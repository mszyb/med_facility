package pl.mszyb.med_facility.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.mszyb.med_facility.Authenticator;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        return Authenticator.redirectLoggedUsersOrReturnViewName(request, "authentication/login");
    }


    @GetMapping("/afterlogin")
    public String showLoggedHomePage(HttpServletRequest request){
        return Authenticator.redirectLoggedUsersOrReturnViewName(request, "redirect:/403");
    }

    @GetMapping("/403")
    public String accessDenied(){
        return "authentication/403";
    }
}
