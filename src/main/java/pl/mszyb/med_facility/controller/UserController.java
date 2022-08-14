package pl.mszyb.med_facility.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mszyb.med_facility.entity.Specialization;
import pl.mszyb.med_facility.service.SpecializationService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final SpecializationService specializationService;

    public UserController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @ModelAttribute("allSpecs")
    public List<Specialization> getSpecializations(){
        return specializationService.findAll();
    }

    @GetMapping("/homepage")
    public String loginPage() {
        return "user/logged_user_homepage";
    }

    @GetMapping("/reservation/search/spec")
    public String showSpecSearchForm(){
        return "user/specialization_search";
    }

}
