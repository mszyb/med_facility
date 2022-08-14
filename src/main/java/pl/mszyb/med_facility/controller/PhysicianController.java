package pl.mszyb.med_facility.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mszyb.med_facility.entity.PhysicianSchedule;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.service.PhysicianScheduleService;
import pl.mszyb.med_facility.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/doc")
public class PhysicianController {

    private final UserService userService;
    private final PhysicianScheduleService physicianScheduleService;

    public PhysicianController(UserService userService, PhysicianScheduleService physicianScheduleService) {
        this.userService = userService;
        this.physicianScheduleService = physicianScheduleService;
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return (User) authentication.getPrincipal();
    }

    @ModelAttribute("currentUserSchedule")
    public List<PhysicianSchedule> getUserSchedule(Model model) {
        User user = getCurrentUser();
        return physicianScheduleService.findAllByPhysicianIdForSelectedPeriod(user.getId());
    }

    @GetMapping("/homepage")
    public String loginPage() {
        return "physician/logged_physician_homepage";
    }

    @GetMapping("/timetable")
    public String showManageTimetable(Model model) {
        model.addAttribute("physicianSchedule", new PhysicianSchedule());
        return "physician/timetable";
    }

    @PostMapping("/timetable/add")
    public String addNewShift(@DateTimeFormat PhysicianSchedule physicianSchedule, Model model) {
        if (physicianSchedule.getStartTime() != null && physicianSchedule.getEndTime() != null) {
            LocalDate startTime = physicianSchedule.getStartTime().toLocalDate();
            LocalDate endTime = physicianSchedule.getEndTime().toLocalDate();
            if (startTime.isEqual(endTime)) {
                physicianSchedule.setPhysician(getCurrentUser());
                physicianScheduleService.save(physicianSchedule);
            } else {
                model.addAttribute("notSameDay", true);
            }
        }
        return "physician/timetable";
    }

    @PostMapping("/search/active_substances")
    public String callNFZApi(@RequestParam String searchValue){
        String uri = "";

    }

}
