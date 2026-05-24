package cz.dominik.ApprovalWorkflow.security;

import cz.dominik.ApprovalWorkflow.entity.User;
import cz.dominik.ApprovalWorkflow.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementace UserDetailsService pro Spring Security.
 * Načítá uživatele z databáze podle emailu při přihlášení.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Uživatel nenalezen"));
        return foundUser;
    }

}
