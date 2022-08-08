package pl.mszyb.med_facility.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomepageController {

    @GetMapping("/")
    public String showHomePage(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/admin/homepage?page=0";
        }
        if (request.isUserInRole("ROLE_USER")) {
            return "redirect:/user/homepage";
        }
        if (request.isUserInRole("ROLE_PHYSICIAN")) {
            return "redirect:/doc/homepage";
        }
        return "index";
    }
}
