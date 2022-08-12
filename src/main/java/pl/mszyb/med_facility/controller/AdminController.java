package pl.mszyb.med_facility.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mszyb.med_facility.entity.*;
import pl.mszyb.med_facility.service.*;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final SpecializationService specializationService;
    private final UserSpecServ_Service userSpecializationService;

    private final ServiceTypeService serviceTypeService;

    public AdminController(UserService userService, RoleService roleService, SpecializationService specializationService, ServiceTypeService serviceTypeService, UserSpecServ_Service userSpecializationService) {
        this.userService = userService;
        this.roleService = roleService;
        this.specializationService = specializationService;
        this.userSpecializationService = userSpecializationService;
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
        model.addAttribute("servicesBySpecializations", userSpecializationService.findSpecializationsAndServicesForUserId(id));
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
    public String SpecializationToUserAssociation(Model model, @RequestParam String specializationName, @RequestParam String userId){
        Specialization specialization = specializationService.findByName(specializationName);
        User user = userService.findById(Long.parseLong(userId)).orElseThrow(NoSuchElementException::new);
        UserServicesSpecializations uss = new UserServicesSpecializations();
        uss.setUser(user);
        uss.setSpecialization(specialization);
        userSpecializationService.save(uss);
        model.addAttribute("user", user);
        model.addAttribute("specializations", specializationService.findAll());
        model.addAttribute("servicesBySpecializations", userSpecializationService.findSpecializationsAndServicesForUserId(Long.parseLong(userId)));
        return "admin/editUserForm";
    }

    // service typy
    // DODAC ASOCJACJE
    @PostMapping("/service_to_specialization_association")
    public String userServiceToSpecializationAssociation(){
        return "admin/editUserForm";
    }

    @GetMapping("/services")
    public String showServicesPage(Model model) {
        model.addAttribute("services", serviceTypeService.findAll());
        model.addAttribute("newServiceType", new ServiceType());
        return "admin/services";
    }

    @GetMapping("/service/delete/{id}")
    public String deleteService(@PathVariable long id) {
        serviceTypeService.remove(id);
        return "redirect:/admin/services";
    }

    @PostMapping("/services/add")
    public String addService(ServiceType newServ) {
        serviceTypeService.save(newServ);
        return "redirect:/admin/services";
    }

}
