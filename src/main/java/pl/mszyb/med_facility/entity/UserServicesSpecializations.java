package pl.mszyb.med_facility.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_specialization_service")
public class UserServicesSpecializations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Specialization specialization;
    @ManyToOne(fetch = FetchType.EAGER)
    private ServiceType service;
}
