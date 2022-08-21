package pl.mszyb.med_facility.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.Appointment;
import pl.mszyb.med_facility.repository.AppointmentRepository;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public List<Appointment> findAllByPhysicianIdForSelectedPeriod(long physicianId, ZonedDateTime toDate, ZonedDateTime fromDate){
        return appointmentRepository.findAllByPhysicianIdForSelectedPeriod(physicianId, toDate, fromDate);
    }

    public List<Appointment> findAllNotFinishedByPhysicianIdForSelectedPeriod(long physicianId, ZonedDateTime toDate, ZonedDateTime fromDate){
        return appointmentRepository.findAllNotFinishedByPhysicianIdForSelectedPeriod(physicianId, toDate, fromDate);
    }

    public void save(Appointment appointment){
        appointmentRepository.save(appointment);
    }

    public List<Appointment> findAllByPatientIdForSelectedPeriod(long patientId, ZonedDateTime toDate, ZonedDateTime fromDate){
        return appointmentRepository.findAllByPatientIdForSelectedPeriod(patientId, toDate, fromDate);
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
}
