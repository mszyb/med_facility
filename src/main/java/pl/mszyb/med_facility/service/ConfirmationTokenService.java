package pl.mszyb.med_facility.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.ConfirmationToken;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.repository.ConfirmationTokenRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserService userService;
    private final RoleService roleService;

    public void save(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public ConfirmationToken findByConfirmationToken(String confirmationToken){
        return confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    }

    public void deleteByConfirmationToken(String confirmationToken){
        confirmationTokenRepository.deleteByConfirmationToken(confirmationToken);
    }

    public void confirmUserEmail(ConfirmationToken tokenFromDb){
        Long userId = tokenFromDb.getUser().getId();
        Optional<User> userOptional = userService.findById(userId);
        userOptional.ifPresent(user -> {
            user.setVerified(true);
            user.setRole(roleService.findRoleByName("ROLE_USER"));
            userService.update(user);
        });
    }
}
