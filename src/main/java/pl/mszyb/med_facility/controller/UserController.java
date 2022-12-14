package pl.mszyb.med_facility.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mszyb.med_facility.entity.*;
import pl.mszyb.med_facility.service.*;

import java.time.ZonedDateTime;
import java.util.*;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final SpecializationService specializationService;
    private final UserSpecServ_Service userSpecServService;
    private final ServiceTypeService serviceTypeService;
    private final AppointmentService appointmentService;

    @ModelAttribute("allSpecs")
    public List<Specialization> getSpecializations() {
        return specializationService.findAllActive();
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/homepage")
    public String loginPage(Model model) {
        ZonedDateTime interval = ZonedDateTime.now().plusDays(14);
        List<Appointment> appointments = appointmentService.findAllNotFinishedByPatientIdForSelectedPeriod(getCurrentUser().getId(), interval, ZonedDateTime.now());
        appointments.sort(Comparator.comparing(Appointment::getStartTime));
        model.addAttribute("appointments", appointments);
        return "user/logged_user_homepage";
    }

    @GetMapping("/reservation/search/spec")
    public String showSpecSearchForm() {
        return "user/specialization_search";
    }

    @PostMapping("/reservation/search/spec")
    public String showServiceSearchForm(@RequestParam Long selectedSpecId, Model model) {
        Specialization selectedSpec = specializationService.findById(selectedSpecId).orElseThrow(NoSuchElementException::new);
        model.addAttribute("allServices", userSpecServService.findAllActiveServicesForSelectedSpecialization(selectedSpec));
        model.addAttribute("selectedSpec", specializationService.findById(selectedSpecId).orElseThrow(NoSuchElementException::new));
        return "user/service_search";
    }

    @PostMapping("/reservation/search/result")
    public String showSearchResult(@RequestParam Long selectedServiceId, @RequestParam Long selectedSpecId, Model model) {
        Specialization selectedSpec = specializationService.findById(selectedSpecId).orElseThrow(NoSuchElementException::new);
        ServiceType selectedService = serviceTypeService.findById(selectedServiceId).orElseThrow(NoSuchElementException::new);
        List<UserServicesSpecializations> filteredUserSpecServ = userSpecServService.findAllForSelectedServiceAndSpecialization(selectedSpec, selectedService);
        model.addAttribute("filteredUserSpecServ", filteredUserSpecServ);
        model.addAttribute("selectedSpec", selectedSpec);
        model.addAttribute("selectedService", selectedService);
        model.addAttribute("appointment", new Appointment());
        Map<Long, List<ZonedDateTime>> availableUsersSlotsMap = userSpecServService.calculateAvailableUserSlotsMap(filteredUserSpecServ);
        model.addAttribute("availableUsersSlotsMap", availableUsersSlotsMap);
        return "user/search_result";
    }

    @PostMapping("/reservation/add")
    public String addNewAppointment(Appointment appointment, Model model) {
        appointmentService.save(appointment);
        model.addAttribute("newAppointment", appointment);
        return "user/appointment_success";
    }

    @GetMapping("/appointment/delete")
    public String deleteAppointment(Appointment appointment, Model model, @RequestParam(required = false) String confirm) {
        if (confirm != null && confirm.equals("yes")) {
            if(!Objects.equals(getCurrentUser().getId(), appointment.getPatient().getId())){
                return "authentication/403";
            }
            appointmentService.deleteById(appointment.getId());
            return "redirect:/user/homepage";
        }
        model.addAttribute("appointment", appointment);
        return "user/appointment_delete_confirmation";
    }

    @GetMapping("/appointment/history")
    public String showAppointmentsHistory(Model model) {
        List<Appointment> doneAppointmentsList = appointmentService.findAllAlreadyDoneByPatientId(getCurrentUser().getId());
        model.addAttribute("doneAppointmentsList", doneAppointmentsList);
        return "user/appointment_history";
    }

}
