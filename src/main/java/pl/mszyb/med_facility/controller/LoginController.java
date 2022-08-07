package pl.mszyb.med_facility.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "authentication/login";
    }


    @GetMapping("/afterlogin")
    public String showLoggedHomePage(){
        return "authentication/logged_homepage";
    }

    @GetMapping("/403")
    public String accessDenied(){
        return "authentication/403";
    }
}
