package pl.mszyb.med_facility.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.service.UserService;

@Controller
public class HomepageController {

    private UserService userService;

    public HomepageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/afterlogin")
    public String showLoggedHomePage(){
        return "logged_homepage";
    }

    @GetMapping("/403")
    public String accessDenied(){
        return "403";
    }
}
