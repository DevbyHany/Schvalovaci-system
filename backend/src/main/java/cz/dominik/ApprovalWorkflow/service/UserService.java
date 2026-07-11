package cz.dominik.ApprovalWorkflow.service;

import cz.dominik.ApprovalWorkflow.dto.RegisterDTO;
import cz.dominik.ApprovalWorkflow.dto.UserResponseDTO;
import cz.dominik.ApprovalWorkflow.entity.Role;
import cz.dominik.ApprovalWorkflow.entity.User;
import cz.dominik.ApprovalWorkflow.exception.InvalidRequestStateException;
import cz.dominik.ApprovalWorkflow.exception.ResourceNotFoundException;
import cz.dominik.ApprovalWorkflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servisní vrstva pro správu uživatelů.
 */
@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Zaregistruje nového uživatele s rolí USER.
     * Heslo je uloženo v hashované podobě pomocí BCrypt.
     *
     * @throws InvalidRequestStateException pokud email již existuje
     */
    public UserResponseDTO register(RegisterDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new InvalidRequestStateException("Email už je zaregistrován");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);

        User saved = userRepository.save(user);
        return new UserResponseDTO(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole());
    }

    /**
     * Aktualizuje čas posledního přihlášení uživatele.
     *
     * @throws ResourceNotFoundException pokud uživatel s daným emailem neexistuje
     */
    public void updateLastLogin(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Uživatel nenalezen"));
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }
}