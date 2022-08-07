package pl.mszyb.med_facility.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "authentication/login";
    }


    @GetMapping("/afterlogin")
    public String showLoggedHomePage(HttpServletRequest request){
        if(request.isUserInRole("ROLE_ADMIN")){
            return "redirect:/admin/homepage";
        }
        if(request.isUserInRole("ROLE_USER")){
            return "redirect:/user/homepage";
        }
        if(request.isUserInRole("ROLE_PHYSICIAN")){
            return "redirect:/doc/homepage";
        }
        return "redirect:/403";
    }

    @GetMapping("/403")
    public String accessDenied(){
        return "authentication/403";
    }
}
