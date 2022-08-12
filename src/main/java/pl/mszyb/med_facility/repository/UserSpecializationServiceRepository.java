package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mszyb.med_facility.entity.ServiceType;
import pl.mszyb.med_facility.entity.Specialization;
import pl.mszyb.med_facility.entity.UserSpecializationService;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface UserSpecializationServiceRepository extends JpaRepository<UserSpecializationService, Long> {

    @Query("select distinct u.specialization.id from UserSpecializationService u where u.user.id = ?1")
    List<Long> findSpecializationForUserId(Long id);

    @Query("select u.service as service, u.specialization as specialization from UserSpecializationService u where u.user.id = ?1")
    Map<Specialization, List<ServiceType>> findServicesBySpecializationsForUserId(Long id);
}

