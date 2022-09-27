package pl.mszyb.med_facility.controller;

import lombok.AllArgsConstructor;
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
import pl.mszyb.med_facility.service.NfzApiService;
import pl.mszyb.med_facility.service.PhysicianScheduleService;

import java.time.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/doc")
@AllArgsConstructor
public class PhysicianController {

    private final AppointmentService appointmentService;
    private final PhysicianScheduleService physicianScheduleService;
    private final NfzApiService nfzApiService;
    private static final String BASE_ACTIVE_SUBSTANCES_URL = "https://api.nfz.gov.pl/app-stat-api-ra/active-substances?page=";
    private static final String BASE_MEDICINE_PRODUCTS_URL = "https://api.nfz.gov.pl/app-stat-api-ra/medicine-products?page=";

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @ModelAttribute("currentUserSchedule")
    public List<PhysicianSchedule> getUserSchedule() {
        User user = getCurrentUser();
        List<PhysicianSchedule> scheduleList = physicianScheduleService.findAllByPhysicianIdForSelectedPeriod(user.getId());
        Collections.sort(scheduleList);
        return scheduleList;
    }

    @ModelAttribute("currentUserAppointments")
    public List<Appointment> getUserAppointments() {
        List<Appointment> appointments = appointmentService.findAllNotFinishedByPhysicianIdForSelectedPeriod(getCurrentUser().getId(), ZonedDateTime.now().plusDays(14), ZonedDateTime.now().minusDays(7));
        appointments.sort(Comparator.comparing(Appointment::getStartTime));
        return appointments;
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
            try {
                physicianScheduleService.addNewShift(date, startTime, endTime, getCurrentUser());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                model.addAttribute("error", true);
                model.addAttribute("errorMessage", e.getMessage());
            }
        }
            return "physician/timetable";
    }

    @GetMapping("/timetable/delete")
    public String deleteShift(PhysicianSchedule shift, Model model, @RequestParam(required = false) String confirm) {
        if ("yes".equals(confirm)) {
            physicianScheduleService.deleteById(shift.getId());
            return "redirect:/doc/homepage";
        }
        model.addAttribute("shift", physicianScheduleService.findById(shift.getId()));
        return "physician/timetable_delete_confirmation";
    }

    @PostMapping("/search/active_substances")
    public String searchForActiveSubstances(@RequestParam String searchValue, @RequestParam String pageNum, Model
            model) {
        nfzApiService.callNFZApi(searchValue, pageNum, model, BASE_ACTIVE_SUBSTANCES_URL);
        return "physician/search_result_active_substances";
    }

    @PostMapping("/search/medicine_products")
    public String searchForMedicineProducts(@RequestParam String searchValue, @RequestParam String pageNum, Model
            model) {
        nfzApiService.callNFZApi(searchValue, pageNum, model, BASE_MEDICINE_PRODUCTS_URL);
        return "physician/search_result_medicine_products";
    }

    @GetMapping("appointment/done")
    public String markAppointmentAsFinished(@RequestParam(defaultValue = "0") long appointmentId) {
        appointmentService.markAppointmentAsFinished(appointmentId);
        return "redirect:/doc/homepage";
    }

    @GetMapping("/appointment/history")
    public String showAppointmentsHistory(Model model) {
        List<Appointment> doneAppointmentsList = appointmentService.findAllAlreadyDoneByPhysicianId(getCurrentUser().getId());
        model.addAttribute("doneAppointmentsList", doneAppointmentsList);
        return "physician/appointment_history";
    }

}
