package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mszyb.med_facility.entity.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    Specialization findByName(String name);
}
