package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mszyb.med_facility.entity.ServiceType;

import java.util.List;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    ServiceType findByName(String name);
}
