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
import pl.mszyb.med_facility.entity.Appointment;
import pl.mszyb.med_facility.entity.PhysicianSchedule;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.service.AppointmentService;
import pl.mszyb.med_facility.service.PhysicianScheduleService;
import pl.mszyb.med_facility.service.UserService;

import java.sql.Time;
import java.time.*;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/doc")
@AllArgsConstructor
public class PhysicianController {

    private final AppointmentService appointmentService;
    private final PhysicianScheduleService physicianScheduleService;

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return (User) authentication.getPrincipal();
    }

    @ModelAttribute("currentUserSchedule")
    public List<PhysicianSchedule> getUserSchedule() {
        User user = getCurrentUser();
        return physicianScheduleService.findAllByPhysicianIdForSelectedPeriod(user.getId());
    }

    @ModelAttribute("currentUserAppointments")
    public List<Appointment> getUserAppointments() {
        ZonedDateTime interval = ZonedDateTime.now().plusDays(14);
        return appointmentService.findAllByPhysicianIdForSelectedPeriod(getCurrentUser().getId(), interval, ZonedDateTime.now());
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
    public String addNewShift(@RequestParam LocalDate date, @RequestParam LocalTime startTime, @RequestParam LocalTime endTime, Model model) {
        if (startTime != null && endTime != null && date != null) {
            if(startTime.isAfter(endTime) || startTime.equals(endTime)){
                model.addAttribute("wrongTime", true);
                return "physician/timetable";
            }
            ZonedDateTime startDateTime = startTime.atDate(date).atZone(ZoneId.of("Europe/Warsaw"));
            ZonedDateTime endDateTime = endTime.atDate(date).atZone(ZoneId.of("Europe/Warsaw"));
            PhysicianSchedule physicianSchedule = new PhysicianSchedule();
            physicianSchedule.setPhysician(getCurrentUser());
            physicianSchedule.setStartTime(startDateTime);
            physicianSchedule.setEndTime(endDateTime);
            physicianScheduleService.save(physicianSchedule);
        }
        return "physician/timetable";
    }

    public void callNFZApi(String searchValue, String pageNum, Model model, String baseUri) {
        if (Integer.parseInt(pageNum) < 1) {
            pageNum = "1";
        }
        String uri = baseUri + pageNum + "&limit=10&format=json&name=" + searchValue;
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = restTemplate.getForObject(uri, String.class);
        JsonActiveSubstancesResponseDto apiResponse = null;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("searchValue", searchValue);
        try {
            apiResponse = mapper.readValue(jsonString, JsonActiveSubstancesResponseDto.class);
            model.addAttribute("apiResponse", apiResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/search/active_substances")
    public String searchForActiveSubstances(@RequestParam String searchValue, @RequestParam String pageNum, Model model) {
        String baseUri = "https://api.nfz.gov.pl/app-stat-api-ra/active-substances?page=";
        callNFZApi(searchValue, pageNum, model, baseUri);
        return "physician/search_result_active_substances";
    }

    @PostMapping("/search/medicine_products")
    public String searchForMedicineProducts(@RequestParam String searchValue, @RequestParam String pageNum, Model model) {
        String baseUri = "https://api.nfz.gov.pl/app-stat-api-ra/medicine-products?page=";
        callNFZApi(searchValue, pageNum, model, baseUri);
        return "physician/search_result_medicine_products";
    }

}
