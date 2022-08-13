package pl.mszyb.med_facility.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.mszyb.med_facility.Authenticator;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.service.RoleService;
import pl.mszyb.med_facility.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegistrationController {
    private final UserService userService;
    private final RoleService roleService;

    public RegistrationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/sign_in")
    public String showRegistrationForm(HttpServletRequest request, Model model) {
        model.addAttribute("user", new User());
        return Authenticator.redirectLoggedUsersOrReturnViewName(request, "registration/registration");
    }

    @PostMapping("/sign_in")
    public String readRegistrationForm(User user) {
        user.setRole(roleService.findRoleByName("ROLE_USER"));
        userService.save(user);
        return "registration/success_registration";
    }

}
