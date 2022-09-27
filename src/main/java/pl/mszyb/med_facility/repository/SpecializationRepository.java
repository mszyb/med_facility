package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mszyb.med_facility.entity.Specialization;

import java.util.List;
import java.util.Optional;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    @Query("select s from Specialization s where s.name=?1")
    Specialization findByName(String name);

    @Query("select s from Specialization s where s.id=?1")
    Optional<Specialization> findById(Long id);

    @Query("select s from Specialization s where s.isActive = true")
    List<Specialization> findAllActive();
}
