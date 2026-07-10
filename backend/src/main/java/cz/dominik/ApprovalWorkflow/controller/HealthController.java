package cz.dominik.ApprovalWorkflow.controller;

import cz.dominik.ApprovalWorkflow.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final UserRepository userRepository;

    public HealthController (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/api/health")
    public Map<String, Object> health() {
        long userCount = userRepository.count();
        return Map.of("status", "UP", "users", userCount);
    }
}
