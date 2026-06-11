package cz.dominik.ApprovalWorkflow.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Konfigurace Spring Security.
 * Používá HTTP Basic Auth a definuje přístupová pravidla pro jednotlivé endpointy.
 */
@Configuration
public class SecurityConfig {


    @Value("${FRONTEND_URL:http://localhost:3000}")
    private String frontendUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(request -> {
            var config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:3000", frontendUrl));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
        })).authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/api/requests").hasAnyRole("USER", "APPROVER").requestMatchers(HttpMethod.GET, "/api/requests/**").authenticated().requestMatchers(HttpMethod.PUT, "/api/requests/*/approve").hasAnyRole("APPROVER", "ADMIN").requestMatchers(HttpMethod.PUT, "/api/requests/*/reject").hasAnyRole("APPROVER", "ADMIN").requestMatchers(HttpMethod.POST, "/api/users/register").permitAll().anyRequest().authenticated()).httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
