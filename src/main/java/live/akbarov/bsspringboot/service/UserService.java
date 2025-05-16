package live.akbarov.bsspringboot.service;

import live.akbarov.bsspringboot.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    UserEntity registerUser(String username, String password, String email, UserEntity.Role role);
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}