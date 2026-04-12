package com.a6dig.digitalsignage.repository;

import com.a6dig.digitalsignage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
<<<<<<< HEAD
}
=======
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
}
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
