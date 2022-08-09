package pl.mszyb.med_facility.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mszyb.med_facility.entity.Role;
import pl.mszyb.med_facility.entity.Specialization;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.service.RoleService;
import pl.mszyb.med_facility.service.ServiceTypeService;
import pl.mszyb.med_facility.service.SpecializationService;
import pl.mszyb.med_facility.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final SpecializationService specializationService;
    private final ServiceTypeService serviceTypeService;

    public AdminController(UserService userService, RoleService roleService, SpecializationService specializationService, ServiceTypeService serviceTypeService) {
        this.userService = userService;
        this.roleService = roleService;
        this.specializationService = specializationService;
        this.serviceTypeService = serviceTypeService;
    }

    @ModelAttribute("roles")
    public Collection<Role> roleList() {
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
    public String showUserEditPage(@PathVariable Long id, Model model) {
        User user = userService.findById(id).orElseThrow(NoSuchElementException::new);
        model.addAttribute("user", user);
        model.addAttribute("specializations", specializationService.findAll());
        return "admin/editUserForm";
    }

    @PostMapping("/homepage/edit/{id}")
    public String readUserEditForm(User editedUser) {
        userService.update(editedUser);
        return "redirect:/admin/homepage?page=0";
    }

    @GetMapping("/specializations")
    public String showSpecializationsPage(Model model) {
        model.addAttribute("specializations", specializationService.findAll());
        model.addAttribute("newSpec", new Specialization());
        return "admin/specializations";
    }

    @GetMapping("/specialization/delete/{id}")
    public String deleteSpecialization(@PathVariable long id) {
        specializationService.remove(id);
        return "redirect:/admin/specializations";
    }

    @PostMapping("/specializations/add")
    public String addSpecialization(Specialization newSpec) {
        specializationService.save(newSpec);
        return "redirect:/admin/specializations";
    }

    @PostMapping("/specialization_to_user_association")
    public String showServiceToUserAssociation(Model model, @RequestParam String specializationName){
        model.addAttribute("services", serviceTypeService.findAllBySpecializationName(specializationName));
        model.addAttribute("specializationName", specializationName);
        return "admin/specialization_and_services_to_user";
    }

}
