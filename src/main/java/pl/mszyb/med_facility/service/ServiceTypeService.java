package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.ServiceType;
import pl.mszyb.med_facility.repository.ServiceTypeRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeService(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    public List<ServiceType> findAllActive() {
        return serviceTypeRepository.findAllActive();
    }

    public List<ServiceType> findAll(){
        return serviceTypeRepository.findAll();
    }

    public void save(ServiceType serviceType) {
        serviceTypeRepository.save(serviceType);
    }

    public void deactivate(long id) {
        ServiceType service = serviceTypeRepository.findById(id).orElseThrow(NoSuchElementException::new);
        service.setActive(false);
        serviceTypeRepository.save(service);
    }
    public void activate(long id) {
        ServiceType service = serviceTypeRepository.findById(id).orElseThrow(NoSuchElementException::new);
        service.setActive(true);
        serviceTypeRepository.save(service);
    }

    public ServiceType findByName(String name) {
        return serviceTypeRepository.findByName(name);
    }

    public Optional<ServiceType> findById(long id){
        return serviceTypeRepository.findById(id);
    }
}
