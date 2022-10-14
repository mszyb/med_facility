package pl.mszyb.med_facility.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mszyb.med_facility.entity.ConfirmationToken;
import pl.mszyb.med_facility.entity.Role;
import pl.mszyb.med_facility.entity.User;
import java.util.Optional;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private ConfirmationTokenService confirmationTokenService;
    private final Long userId = new Random().nextLong();
    private final User someUser = new User();
    private final Role someRole = new Role();
    private final User expectedUser = new User();
    private final Role expectedRole = new Role();
    private final ConfirmationToken confirmationToken = new ConfirmationToken();

    @BeforeEach
    void setUp() {
        someRole.setRoleName("SOME_ROLE");
        someUser.setVerified(false);
        someUser.setId(userId);
        someUser.setRole(someRole);
        expectedRole.setRoleName("ROLE_USER");
        expectedUser.setVerified(true);
        expectedUser.setRole(expectedRole);
        confirmationToken.setUser(someUser);
    }

    @Test
    void should_verified_user() {
        given(userService.findById(someUser.getId())).willReturn(Optional.of(someUser));
        given(roleService.findRoleByName("ROLE_USER")).willReturn(expectedRole);
        confirmationTokenService.confirmUserEmail(confirmationToken);
        assertEquals(expectedUser.getRole().getRoleName(), someUser.getRole().getRoleName());
    }

    @Test
    void should_not_verified_user() {
        given(userService.findById(someUser.getId())).willReturn(Optional.empty());
        confirmationTokenService.confirmUserEmail(confirmationToken);
        assertEquals("SOME_ROLE", someUser.getRole().getRoleName());
    }
}