package pl.mszyb.med_facility.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mszyb.med_facility.entity.Role;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.service.RoleService;
import pl.mszyb.med_facility.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @ModelAttribute("roles")
    public Collection<Role> roleList(){
        return roleService.findAll();
    }

    @GetMapping("/homepage")
    public String loginPage(@RequestParam String page, Model model) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 5, Sort.by("created"));
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
    public String showUserEditPage(@PathVariable Long id, Model model){
        User user = userService.findById(id).orElseThrow(NoSuchElementException::new);
        model.addAttribute("user", user);
        return "admin/editUserForm";
    }

    @PostMapping("/homepage/edit/{id}")
    public String readUserEditForm(User editedUser){
        userService.update(editedUser);
        return "redirect:/admin/homepage?page=0";
    }

    @GetMapping("/specializations")
    public String showSpecializationsPage(Model model){

        return "admin/editSpecializations";
    }

}
