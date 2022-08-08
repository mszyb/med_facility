package pl.mszyb.med_facility.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/homepage")
    public String loginPage(@RequestParam String page, Model model) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10);
        Page<User> userPage = userService.findAll(pageable);
        List<User> users = userPage.getContent();
        model.addAttribute("users", users);
        model.addAttribute("nextPage", Integer.parseInt(page) + 1);
        model.addAttribute("hasPrevious", pageable.hasPrevious());
        model.addAttribute("isPageable", pageable.isPaged());
        model.addAttribute("totalPages", userPage.getTotalPages());
        return "admin/logged_admin_homepage";
    }

    @GetMapping("/homepage/edit/{id}")
    public String showUserEditPage(@PathVariable Long id){
        User user = userService.findById(id).orElseThrow(NoSuchElementException::new);
        return "editUserForm";
    }

}
