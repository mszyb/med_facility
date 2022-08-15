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
        return appointmentRepository.findAllByPhysicianIdForSelectedPeriod(physicianId, toDate, ZonedDateTime.now());
    }
}
