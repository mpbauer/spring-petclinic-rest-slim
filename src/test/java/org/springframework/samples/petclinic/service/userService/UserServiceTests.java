package org.springframework.samples.petclinic.service.userService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"h2"})
class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    void shouldAddUser() throws Exception {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEnabled(true);
        user.addRole("OWNER_ADMIN");

        userService.saveUser(user);
        assertThat(user.getRoles().parallelStream().allMatch(role -> role.getName().startsWith("ROLE_")), is(true));
        assertThat(user.getRoles().parallelStream().allMatch(role -> role.getUser() != null), is(true));
    }
}
