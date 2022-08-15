package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mszyb.med_facility.entity.Appointment;
import pl.mszyb.med_facility.entity.PhysicianSchedule;

import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("select a from Appointment a where a.physician.id = ?1 and a.startTime < ?2 and a.endTime>=?3")
    List<Appointment> findAllByPhysicianIdForSelectedPeriod(Long physicianId, ZonedDateTime toDate, ZonedDateTime fromDate);


}
