package com.a6dig.digitalsignage.IntegrationTest.repository;

import com.a6dig.digitalsignage.entity.User;
import com.a6dig.digitalsignage.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail(){
        User user = new User();
        user.setUsername("johndoe");
        user.setEmail("john.doe@email.com");
        user.setPassword("pass");

        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("john.doe@email.com");

        assertTrue(result.isPresent());

    }
}
