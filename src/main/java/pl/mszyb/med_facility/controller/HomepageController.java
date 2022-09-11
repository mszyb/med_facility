package pl.mszyb.med_facility.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.mszyb.med_facility.Authenticator;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomepageController {

    @GetMapping("/")
    public String showHomePage(HttpServletRequest request) {
        return Authenticator.redirectLoggedUsersOrReturnViewName(request, "index");
    }
}
