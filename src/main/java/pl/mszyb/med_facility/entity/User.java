package pl.mszyb.med_facility.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import net.bytebuddy.build.Plugin;
import org.hibernate.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @NotNull
    private String firstName;
    @NotEmpty
    @NotNull
    private String lastName;
    private String phoneNumber;
    @NotNull
    @NotEmpty
    @Column(unique = true)
    @Email
    private String email;
    @Size(min = 4)
    @NotNull
    @NotEmpty
    private String password;
    @ManyToMany
    private List<Specialization> specializations;
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;
    @ManyToMany
    private List<ServiceType> services;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
    }
    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
