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
    private final PhysicianScheduleService physicianScheduleService;
    private final AppointmentService appointmentService;

    @ModelAttribute("allSpecs")
    public List<Specialization> getSpecializations() {
        return specializationService.findAll();
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/homepage")
    public String loginPage() {
        return "user/logged_user_homepage";
    }

    @GetMapping("/reservation/search/spec")
    public String showSpecSearchForm() {
        return "user/specialization_search";
    }

    @PostMapping("/reservation/search/spec")
    public String showServiceSearchForm(@RequestParam Long selectedSpecId, Model model) {
        Specialization selectedSpec = specializationService.findById(selectedSpecId).orElseThrow(NoSuchElementException::new);
        model.addAttribute("allServices", userSpecServService.findAllServicesForSelectedSpecialization(selectedSpec));
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
        Map<Long, List<ZonedDateTime>> availableUsersSlotsMap = new HashMap<>();
        for (UserServicesSpecializations uss : filteredUserSpecServ) {
            long currentPhysicianId = uss.getUser().getId();
            availableUsersSlotsMap.put(currentPhysicianId, physicianScheduleService.calculateAvailableSlots(currentPhysicianId));
        }
        model.addAttribute("availableUsersSlotsMap", availableUsersSlotsMap);
        return "user/search_result";
    }

    @PostMapping("/reservation/add")
    public String addNewAppointment(Appointment appointment, Model model) {
        appointmentService.save(appointment);
        model.addAttribute("newAppointment", appointment);
        return "user/appointment_success";
    }
}
