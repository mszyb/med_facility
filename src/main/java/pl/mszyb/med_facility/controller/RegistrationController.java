package pl.mszyb.med_facility.controller;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.mszyb.med_facility.Authenticator;
import pl.mszyb.med_facility.entity.ConfirmationToken;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.service.ConfirmationTokenService;
import pl.mszyb.med_facility.service.EmailSenderService;
import pl.mszyb.med_facility.service.RoleService;
import pl.mszyb.med_facility.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final RoleService roleService;
    private final EmailSenderService emailSenderService;
    private final ConfirmationTokenService confirmationTokenService;


    @GetMapping("/sign_in")
    public String showRegistrationForm(HttpServletRequest request, Model model) {
        model.addAttribute("user", new User());
        return Authenticator.redirectLoggedUsersOrReturnViewName(request, "registration/registration");
    }

    @PostMapping("/sign_in")
    public String readRegistrationForm(User user, Model model) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("alreadyExist", true);
            return "registration/registration";
        }
            user.setRole(roleService.findRoleByName("ROLE_UNVERIFIED"));
            userService.save(user);
            SimpleMailMessage mailMessage = emailSenderService.createRegistrationMail(user);
            emailSenderService.sendMail(mailMessage);
            return "registration/success_registration";
    }

    @GetMapping("/sign_in/confirmation")
    public String confirmEmail(@RequestParam String token) {
        ConfirmationToken tokenFromDb = confirmationTokenService.findByConfirmationToken(token);
        if (tokenFromDb != null) {
            if (tokenFromDb.getUser().isVerified()) {
                return "authentication/broken_link";
            }
            confirmationTokenService.confirmUserEmail(tokenFromDb);
            return "registration/account_confirmed";
        }
        return "authentication/broken_link";
    }
}
