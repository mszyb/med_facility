package pl.mszyb.med_facility.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mszyb.med_facility.entity.*;
import pl.mszyb.med_facility.service.PhysicianScheduleService;
import pl.mszyb.med_facility.service.ServiceTypeService;
import pl.mszyb.med_facility.service.SpecializationService;
import pl.mszyb.med_facility.service.UserSpecServ_Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final SpecializationService specializationService;
    private final UserSpecServ_Service userSpecServService;
    private final ServiceTypeService serviceTypeService;
    private final PhysicianScheduleService physicianScheduleService;

    @ModelAttribute("allSpecs")
    public List<Specialization> getSpecializations() {
        return specializationService.findAll();
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
        List<List<ZonedDateTime>> availableUsersSlots = new ArrayList<>();
        for(UserServicesSpecializations uss : filteredUserSpecServ){
            long currentPhysicianId = uss.getUser().getId();
            availableUsersSlots.add(physicianScheduleService.calculateAvailableSlots(currentPhysicianId));
        }
        model.addAttribute("availableUsersSlots", availableUsersSlots);
        return "user/search_result";
    }

}
