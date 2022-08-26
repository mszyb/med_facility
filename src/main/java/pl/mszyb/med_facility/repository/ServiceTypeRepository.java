package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mszyb.med_facility.entity.ServiceType;

import java.util.List;
import java.util.Optional;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    @Query("select s from ServiceType s where s.name=?1")
    ServiceType findByName(String name);

    @Query("select s from ServiceType s where s.id=?1")
    Optional<ServiceType> findById(long id);

    @Query("select s from ServiceType s where s.isActive = true")
    List<ServiceType> findAllActive();

}
