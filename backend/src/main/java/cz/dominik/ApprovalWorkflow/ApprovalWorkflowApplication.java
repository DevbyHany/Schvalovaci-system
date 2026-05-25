package cz.dominik.ApprovalWorkflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ApprovalWorkflowApplication {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("heslo123"));
        SpringApplication.run(ApprovalWorkflowApplication.class, args);
    }

}
