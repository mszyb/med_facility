package pl.mszyb.med_facility.service.user_spec_serv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mszyb.med_facility.entity.*;
import pl.mszyb.med_facility.repository.UserServicesSpecializationsRepository;
import pl.mszyb.med_facility.service.ServiceTypeService;
import pl.mszyb.med_facility.service.SpecializationService;
import pl.mszyb.med_facility.service.UserSpecServ_Service;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserSpecServ_ServiceTest {

    @Mock
    private UserServicesSpecializationsRepository userServicesSpecializationsRepository;
    @Mock
    private SpecializationService specializationService;
    @Mock
    private ServiceTypeService serviceTypeService;
    @InjectMocks
    private UserSpecServ_Service uss_serv;
    private ServiceType serv1;
    private ServiceType serv2;
    private ServiceType serv3;
    private ServiceType serv4;
    private ServiceType serv5;
    private Specialization spec1;
    private Specialization spec2;
    private UserServicesSpecializations uss1;
    private UserServicesSpecializations uss2;
    private UserServicesSpecializations uss3;
    private User physician1;
    private  List<UserServicesSpecializations> servicesAndSpecializations;

    @BeforeEach
    void setUp() {
        serv1 = new ServiceType();
        serv1.setName(ServAndSpecData.SERVICE_1);
        serv2 = new ServiceType();
        serv2.setName(ServAndSpecData.SERVICE_2);
        serv3 = new ServiceType();
        serv3.setName(ServAndSpecData.SERVICE_3);
        serv4 = new ServiceType();
        serv4.setName(ServAndSpecData.SERVICE_4);
        serv5 = new ServiceType();
        serv5.setName(ServAndSpecData.SERVICE_5);
        spec1 = new Specialization();
        spec1.setName(ServAndSpecData.SPEC_1);
        spec2 = new Specialization();
        spec2.setName(ServAndSpecData.SPEC_2);
        uss1 = new UserServicesSpecializations();
        uss2 = new UserServicesSpecializations();
        uss3 = new UserServicesSpecializations();
        physician1 = new User();
        physician1.setId(1L);
        specializationsAndServicesAssociation();
    }

    @Test
    void should_find_2_specs_and_3_services() {
        given(userServicesSpecializationsRepository.findServicesBySpecializationsForUserId(physician1.getId())).willReturn(servicesAndSpecializations);
        Map<Specialization, List<ServiceType>> servicesMap = uss_serv.findSpecializationsAndServicesForUserId(physician1.getId());
        assertNotNull(servicesMap);
        assertEquals(2, servicesMap.keySet().size());
        assertEquals(2, servicesMap.get(spec1).size());
        assertEquals(1, servicesMap.get(spec2).size());
        assertEquals(serv1, servicesMap.get(spec1).get(0));
        assertEquals(serv2, servicesMap.get(spec1).get(1));
        assertEquals(serv3, servicesMap.get(spec2).get(0));
    }

    @Test
    void should_associate_2_extra_services_to_existing_specialization() throws ServiceTypeAlreadyAssignedException {
        given(specializationService.findByName(spec1.getName())).willReturn(spec1);
        given(serviceTypeService.findByName(serv4.getName())).willReturn(serv4);
        given(serviceTypeService.findByName(serv5.getName())).willReturn(serv5);
        List<String> servicesList = Arrays.asList(serv4.getName(), serv5.getName());
        UserSpecServ_Service spyUss_serv = Mockito.spy(uss_serv);
        spyUss_serv.specializationToUserAssociation(spec1.getName(), physician1.getId(), servicesList, physician1);
        verify(spyUss_serv, times(2)).save(any());
    }

    @Test
    void should_throw_ServiceTypeAlreadyAssignedException(){
        given(specializationService.findByName(spec1.getName())).willReturn(spec1);
        given(serviceTypeService.findByName(serv1.getName())).willReturn(serv1);
        given(userServicesSpecializationsRepository.findServicesBySpecializationsForUserId(physician1.getId())).willReturn(servicesAndSpecializations);
        assertThrows(ServiceTypeAlreadyAssignedException.class, () -> uss_serv.specializationToUserAssociation(spec1.getName(), physician1.getId(), Arrays.asList(serv1.getName()), physician1));
    }

    private void specializationsAndServicesAssociation() {
        uss1.setUser(physician1);
        uss1.setSpecialization(spec1);
        uss1.setService(serv1);
        uss2.setUser(physician1);
        uss2.setSpecialization(spec1);
        uss2.setService(serv2);
        uss3.setUser(physician1);
        uss3.setSpecialization(spec2);
        uss3.setService(serv3);
        servicesAndSpecializations = Arrays.asList(uss1, uss2, uss3);
    }
}