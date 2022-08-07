package pl.mszyb.med_facility.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doc")
public class PhysicianController {

    @GetMapping("/homepage")
    public String loginPage() {
        return  "authentication/logged_physician_homepage";
    }

}
