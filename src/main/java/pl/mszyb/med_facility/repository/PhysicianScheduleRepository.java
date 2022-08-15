package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mszyb.med_facility.entity.PhysicianSchedule;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface PhysicianScheduleRepository extends JpaRepository<PhysicianSchedule, Long> {

    @Query("select ps from PhysicianSchedule ps where ps.physician.id = ?1 and ps.startTime < ?2 and ps.endTime>=?3")
    List<PhysicianSchedule> findAllByPhysicianIdForSelectedPeriod(Long physicianId, ZonedDateTime toDate, ZonedDateTime fromDate);
}
