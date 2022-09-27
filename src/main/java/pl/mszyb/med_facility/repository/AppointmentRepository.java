package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mszyb.med_facility.entity.Appointment;
import pl.mszyb.med_facility.entity.PhysicianSchedule;

import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("select a from Appointment a where a.physician.id = ?1 and a.startTime < ?2 and a.endTime>=?3")
    List<Appointment> findAllByPhysicianIdForSelectedPeriod(long physicianId, ZonedDateTime toDate, ZonedDateTime fromDate);

    @Query("select a from Appointment a where a.physician.id = ?1 and a.startTime < ?2 and a.endTime>=?3 and a.isDone = false")
    List<Appointment> findAllNotFinishedByPhysicianIdForSelectedPeriod(long physicianId, ZonedDateTime toDate, ZonedDateTime fromDate);

    @Query("select a from Appointment a where a.patient.id = ?1 and a.startTime < ?2 and a.endTime>=?3 and a.isDone = false")
    List<Appointment> findAllNotFinishedByPatientIdForSelectedPeriod(long patientId, ZonedDateTime toDate, ZonedDateTime fromDate);

    @Query("select a from Appointment a where a.patient.id = ?1 and a.startTime < ?2 and a.endTime>=?3")
    List<Appointment> findAllByPatientIdForSelectedPeriod(long patientId, ZonedDateTime toDate, ZonedDateTime fromDate);

    Appointment findById(long appointmentId);

    void deleteById(long appointmentId);

    @Query("select a from Appointment a where a.patient.id = ?1 and a.isDone = true")
    List<Appointment> findAllAlreadyDoneByPatientId(long patientId);

    @Query("select a from Appointment a where a.physician.id = ?1 and a.isDone = true")
    List<Appointment> findAllAlreadyDoneByPhysicianId(long patientId);
}
