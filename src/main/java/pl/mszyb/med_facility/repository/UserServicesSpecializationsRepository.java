package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mszyb.med_facility.entity.ServiceType;
import pl.mszyb.med_facility.entity.Specialization;
import pl.mszyb.med_facility.entity.UserServicesSpecializations;

import java.util.List;
import java.util.Map;

public interface UserServicesSpecializationsRepository extends JpaRepository<UserServicesSpecializations, Long> {

    @Query("select distinct u.specialization.id from UserServicesSpecializations u where u.user.id = ?1")
    List<Long> findSpecializationForUserId(Long id);

    @Query("select u from UserServicesSpecializations u where u.user.id = ?1")
    List<UserServicesSpecializations> findServicesBySpecializationsForUserId(Long id);

    void removeById(long id);

    UserServicesSpecializations findByServiceIdAndSpecializationId(Long serviceId, Long specId);
    @Query("select distinct (uss.service) from UserServicesSpecializations uss where uss.specialization = ?1")
    List<ServiceType> findAllServicesForSelectedSpecialization(Specialization specialization);
    @Query("select uss from UserServicesSpecializations uss where uss.specialization = ?1 and uss.service = ?2")
    List<UserServicesSpecializations> findAllForSelectedServiceAndSpecialization(Specialization specialization, ServiceType serviceType);
}

