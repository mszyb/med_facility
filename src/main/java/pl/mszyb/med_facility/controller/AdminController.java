package pl.mszyb.med_facility.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mszyb.med_facility.entity.*;
import pl.mszyb.med_facility.service.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final SpecializationService specializationService;
    private final UserSpecServ_Service userSpecializationService;
    private final ServiceTypeService serviceTypeService;

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
        model.addAttribute("services", serviceTypeService.findAll());
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
    public String SpecializationToUserAssociation(Model model, @RequestParam String specializationName, @RequestParam Long userId, @RequestParam List<String> servicesNames) {
        Specialization specialization = specializationService.findByName(specializationName);
        User user = userService.findById(userId).orElseThrow(NoSuchElementException::new);
        Map<Specialization, List<ServiceType>> servicesBySpecializations = userSpecializationService.findSpecializationsAndServicesForUserId(userId);
        for (String name : servicesNames) {
            ServiceType serv = serviceTypeService.findByName(name);
            if (!servicesBySpecializations.containsKey(specialization) || !servicesBySpecializations.get(specialization).contains(serv)) {
                UserServicesSpecializations uss = new UserServicesSpecializations();
                uss.setUser(user);
                uss.setSpecialization(specialization);
                uss.setService(serv);
                userSpecializationService.save(uss);
            } else {
                model.addAttribute("serviceAlreadyAssigned", true);
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("specializations", specializationService.findAll());
        model.addAttribute("services", serviceTypeService.findAll());
        model.addAttribute("servicesBySpecializations", userSpecializationService.findSpecializationsAndServicesForUserId(userId));
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

    @GetMapping("/service_from_spec/delete")
    public String deleteServiceFromUserSpec(@RequestParam Long ussId, Model model, @RequestParam Long userId, @RequestParam Long specId){
       UserServicesSpecializations uss = userSpecializationService.findByServiceAndSpec(ussId, specId);
        userSpecializationService.remove(uss.getId());
        User user = userService.findById(userId).orElseThrow(NoSuchElementException::new);
        model.addAttribute("user", user);
        model.addAttribute("specializations", specializationService.findAll());
        model.addAttribute("services", serviceTypeService.findAll());
        model.addAttribute("servicesBySpecializations", userSpecializationService.findSpecializationsAndServicesForUserId(user.getId()));
        return "admin/editUserForm";
    }

}
