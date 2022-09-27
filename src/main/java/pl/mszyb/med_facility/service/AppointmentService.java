package pl.mszyb.med_facility.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.mszyb.med_facility.entity.Appointment;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserService userService;

    public List<Appointment> findAllByPhysicianIdForSelectedPeriod(long physicianId, ZonedDateTime toDate, ZonedDateTime fromDate){
        return appointmentRepository.findAllByPhysicianIdForSelectedPeriod(physicianId, toDate, fromDate);
    }

    public List<Appointment> findAllNotFinishedByPhysicianIdForSelectedPeriod(long physicianId, ZonedDateTime toDate, ZonedDateTime fromDate){
        return appointmentRepository.findAllNotFinishedByPhysicianIdForSelectedPeriod(physicianId, toDate, fromDate);
    }

    public void save(Appointment appointment){
        appointmentRepository.save(appointment);
    }

    public List<Appointment> findAllNotFinishedByPatientIdForSelectedPeriod(long patientId, ZonedDateTime toDate, ZonedDateTime fromDate){
        return appointmentRepository.findAllNotFinishedByPatientIdForSelectedPeriod(patientId, toDate, fromDate);
    }

    public Appointment findById(long appointmentId){
        return appointmentRepository.findById(appointmentId);
    }

    public void deleteById(long appointmentId){
        appointmentRepository.deleteById(appointmentId);
    }
    public List<Appointment> findAllAlreadyDoneByPatientId(long id){
        return appointmentRepository.findAllAlreadyDoneByPatientId(id);
    }

    public List<Appointment> findAllAlreadyDoneByPhysicianId(long id){
        return appointmentRepository.findAllAlreadyDoneByPhysicianId(id);
    }

    public List<Appointment> findAllByPatientIdForSelectedPeriod(long patientId, ZonedDateTime toDate, ZonedDateTime fromDate){
        return appointmentRepository.findAllByPatientIdForSelectedPeriod(patientId, toDate, fromDate);
    }

    public List<Appointment> findPhysicianAppointments(String physicianEmail, LocalDate date){
            User physician = userService.findByEmail(physicianEmail).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "physician with this email address not found"));
            ZonedDateTime startDateTime = LocalTime.of(00, 01).atDate(date).atZone(ZoneId.of("Europe/Warsaw"));
            ZonedDateTime endDateTime = LocalTime.of(23, 59).atDate(date).atZone(ZoneId.of("Europe/Warsaw"));
            return findAllByPhysicianIdForSelectedPeriod(physician.getId(), endDateTime, startDateTime);
    }

    public List<Appointment> findPatientAppointments(String patientEmail, LocalDate date){
        User patient = userService.findByEmail(patientEmail).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "patient with this email address not found"));
        ZonedDateTime startDateTime = LocalTime.of(00, 01).atDate(date).atZone(ZoneId.of("Europe/Warsaw"));
        ZonedDateTime endDateTime = LocalTime.of(23, 59).atDate(date).atZone(ZoneId.of("Europe/Warsaw"));
        return findAllByPatientIdForSelectedPeriod(patient.getId(), endDateTime, startDateTime);
    }

    public void markAppointmentAsFinished(long appointmentId){
        if (appointmentId != 0) {
            Appointment appointment = findById(appointmentId);
            if (appointment != null) {
                appointment.setDone(true);
                save(appointment);
            }
        }
    }
}
