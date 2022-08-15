package pl.mszyb.med_facility.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pl.mszyb.med_facility.DTO.JsonActiveSubstancesResponseDto;
import pl.mszyb.med_facility.entity.PhysicianSchedule;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.service.PhysicianScheduleService;
import pl.mszyb.med_facility.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/doc")
@AllArgsConstructor
public class PhysicianController {

    private final UserService userService;
    private final PhysicianScheduleService physicianScheduleService;

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
    public String callNFZApi(@RequestParam String searchValue, @RequestParam String pageNum,Model model){
        if(Integer.parseInt(pageNum)<1){
            pageNum = "1";
        }
        String uri = "https://api.nfz.gov.pl/app-stat-api-ra/active-substances?page=" + pageNum + "&limit=10&format=json&name="+ searchValue;
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = restTemplate.getForObject(uri, String.class);
        JsonActiveSubstancesResponseDto apiResponse = null;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("searchValue", searchValue);
        try {
            apiResponse = mapper.readValue(jsonString, JsonActiveSubstancesResponseDto.class);
            model.addAttribute("apiResponse",apiResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "physician/search_result";
    }

}
