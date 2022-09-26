package pl.mszyb.med_facility.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.mszyb.med_facility.entity.*;
import pl.mszyb.med_facility.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final SpecializationService specializationService;
    private final UserSpecServ_Service userSpecializationService;
    private final ServiceTypeService serviceTypeService;
    private final AppointmentService appointmentService;

    private final PhysicianScheduleService physicianScheduleService;

    @ModelAttribute("roles")
    public Collection<Role> roleList() {
        return roleService.findAll();
    }

    @GetMapping("/homepage")
    public String loginPage(@RequestParam Integer page, Model model) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("created").descending());
        Page<User> userPage = userService.findAllWithPagination(pageable);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("hasPrevious", pageable.hasPrevious());
        model.addAttribute("totalPages", userPage.getTotalPages());
        return "admin/logged_admin_homepage";
    }

    @GetMapping("/homepage/edit/{id}")
    public String showUserEditPage(@PathVariable Long id, Model model) {
        User user = userService.findById(id).orElseThrow(NoSuchElementException::new);
        model.addAttribute("user", user);
        model.addAttribute("specializations", specializationService.findAllActive());
        model.addAttribute("services", serviceTypeService.findAllActive());
        model.addAttribute("servicesBySpecializations", userSpecializationService.findSpecializationsAndServicesForUserId(id));
        return "admin/editUserForm";
    }

    @PostMapping("/homepage/edit/{id}")
    public String readUserEditForm(User editedUser) {
        userService.update(editedUser);
        return "redirect:/admin/homepage?page=0";
    }

    @GetMapping("/specializations")
    public String showSpecializationsPage(Model model) {
        model.addAttribute("specializations", specializationService.findAll());
        model.addAttribute("newSpec", new Specialization());
        return "admin/specializations";
    }

    @GetMapping("/specialization/deactivate/{id}")
    public String deactivateSpecialization(@PathVariable long id) {
        specializationService.deactivate(id);
        return "redirect:/admin/specializations";
    }

    @GetMapping("/specialization/activate/{id}")
    public String activateSpecialization(@PathVariable long id) {
        specializationService.activate(id);
        return "redirect:/admin/specializations";
    }

    @PostMapping("/specializations/add")
    public String addSpecialization(Specialization newSpec) {
        specializationService.save(newSpec);
        return "redirect:/admin/specializations";
    }

    @PostMapping("/specialization_to_user_association")
    public String SpecializationToUserAssociation(Model model, @RequestParam String specializationName, @RequestParam Long userId, @RequestParam List<String> servicesNames) {
        User user = userService.findById(userId).orElseThrow(NoSuchElementException::new);
        try {
            userSpecializationService.specializationToUserAssociation(specializationName, userId, servicesNames, user);
        } catch (ServiceTypeAlreadyAssignedException e) {
            model.addAttribute("serviceAlreadyAssigned", true);
        }
        model.addAttribute("user", user);
        model.addAttribute("specializations", specializationService.findAllActive());
        model.addAttribute("services", serviceTypeService.findAllActive());
        model.addAttribute("servicesBySpecializations", userSpecializationService.findSpecializationsAndServicesForUserId(userId));
        return "admin/editUserForm";
    }

    @GetMapping("/services")
    public String showServicesPage(Model model) {
        model.addAttribute("services", serviceTypeService.findAll());
        model.addAttribute("newServiceType", new ServiceType());
        return "admin/services";
    }

    @GetMapping("/service/deactivate/{id}")
    public String deactivateService(@PathVariable long id) {
        serviceTypeService.deactivate(id);
        return "redirect:/admin/services";
    }

    @GetMapping("/service/activate/{id}")
    public String activateService(@PathVariable long id) {
        serviceTypeService.activate(id);
        return "redirect:/admin/services";
    }


    @PostMapping("/services/add")
    public String addService(ServiceType newServ) {
        serviceTypeService.save(newServ);
        return "redirect:/admin/services";
    }

    @GetMapping("/service_from_spec/delete")
    public String deleteServiceFromUserSpec(@RequestParam Long ussId, Model model, @RequestParam Long userId, @RequestParam Long specId) {
        UserServicesSpecializations uss = userSpecializationService.findByServiceAndSpec(ussId, specId);
        userSpecializationService.remove(uss.getId());
        User user = userService.findById(userId).orElseThrow(NoSuchElementException::new);
        model.addAttribute("user", user);
        model.addAttribute("specializations", specializationService.findAllActive());
        model.addAttribute("services", serviceTypeService.findAllActive());
        model.addAttribute("servicesBySpecializations", userSpecializationService.findSpecializationsAndServicesForUserId(user.getId()));
        return "admin/editUserForm";
    }

    @GetMapping("/appointment/search")
    public String searchForAppointment(@RequestParam(required = false) String physicianEmail, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String patientEmail, Model model) {
        if (physicianEmail != null && !physicianEmail.isEmpty()) {
            model.addAttribute("appointments", appointmentService.findPhysicianAppointments(physicianEmail, date));
            model.addAttribute("userEmail", physicianEmail);
        }
        if (patientEmail != null && !patientEmail.isEmpty()) {
            model.addAttribute("appointments", appointmentService.findPatientAppointments(patientEmail, date));
            model.addAttribute("userEmail", patientEmail);
        }
        return "admin/searchForAppointmentResult";
    }

    @GetMapping("/appointment/delete")
    public String deleteAppointment(Appointment appointment) {
        appointmentService.deleteById(appointment.getId());
        return "redirect:/";
    }

    @GetMapping("/appointment/edit")
    public String editAppointment(Appointment appointment, Model model) {
        List<UserServicesSpecializations> filteredUserSpecServ = userSpecializationService.findAllForSelectedServiceAndSpecialization(appointment.getSelectedSpec(), appointment.getSelectedService());
        model.addAttribute("filteredUserSpecServ", filteredUserSpecServ);
        model.addAttribute("selectedSpec", appointment.getSelectedSpec());
        model.addAttribute("selectedService", appointment.getSelectedService());
        model.addAttribute("appointment", appointment);
        Map<Long, List<ZonedDateTime>> availableUsersSlotsMap = userSpecializationService.calculateAvailableUserSlotsMap(filteredUserSpecServ);
        model.addAttribute("availableUsersSlotsMap", availableUsersSlotsMap);
        return "admin/editAppointment";
    }


    @PostMapping("/appointment/add")
    public String addNewDate(Appointment appointment) {
        appointmentService.save(appointment);
        return "redirect:/";
    }

}
