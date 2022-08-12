package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.ServiceType;
import pl.mszyb.med_facility.repository.ServiceTypeRepository;

import java.util.List;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeService (ServiceTypeRepository serviceTypeRepository){
        this.serviceTypeRepository = serviceTypeRepository;
    }

    public List<ServiceType> findAll(){
        return serviceTypeRepository.findAll();
    }

    public void save(ServiceType serviceType){
        serviceTypeRepository.save(serviceType);
    }

    public void remove(long id){
        serviceTypeRepository.deleteById(id);
    }
}
