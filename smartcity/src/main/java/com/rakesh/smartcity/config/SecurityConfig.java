package com.rakesh.smartcity.config;

import com.rakesh.smartcity.service.CustomUserDetailsService;
import com.rakesh.smartcity.service.JwtFilterChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Autowired
    private JwtFilterChainService jwtAuthFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC

                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        //COMPLAIN
                        // Only USER can raise a complain
                        .requestMatchers(HttpMethod.POST,  "/api/complains/create")
                        .hasRole("USER")

                        // USER sees own complains, WORKER & ADMIN can also see
                        .requestMatchers(HttpMethod.GET,   "/api/complains/user/**")
                        .hasAnyRole("USER", "ADMIN")

                        // Only ADMIN can query by adminId
                        .requestMatchers(HttpMethod.GET,   "/api/complains/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/complains/all")
                        .hasRole("ADMIN")


                        // WORKER & ADMIN can see complains by workerId
                        .requestMatchers(HttpMethod.GET,   "/api/complains/worker/**")
                        .hasAnyRole("WORKER", "ADMIN")

                        // WORKER & ADMIN can see complains by pincode area
                        .requestMatchers(HttpMethod.GET,   "/api/complains/pincode/**")
                        .hasAnyRole("WORKER", "ADMIN")

                        // Get single complain by ID → all 3 roles
                        .requestMatchers(HttpMethod.GET,   "/api/complains/**")
                        .hasAnyRole("USER", "WORKER", "ADMIN")

                        // Only ADMIN assigns a worker to a complain
                        .requestMatchers(HttpMethod.POST,  "/api/complains/*/assign-worker/**")
                        .hasRole("ADMIN")

                        // WORKER & ADMIN can update complain status
                        .requestMatchers(HttpMethod.PUT,   "/api/complains/*/status")
                        .hasAnyRole("WORKER", "ADMIN")


                        // Only ADMIN can create a worker
                        .requestMatchers(HttpMethod.POST,  "/api/workers/create")
                        .hasRole("ADMIN")

                        // ADMIN sees all workers list
                        .requestMatchers(HttpMethod.GET,   "/api/workers")
                        .hasRole("ADMIN")

                        // WORKER can see own profile, ADMIN sees any
                        .requestMatchers(HttpMethod.GET,   "/api/workers/**")
                        .hasAnyRole("WORKER", "ADMIN")

                        // ADMIN & WORKER can search by pincode
                        .requestMatchers(HttpMethod.GET,   "/api/workers/pincode")
                        .hasAnyRole("WORKER", "ADMIN")

                        // Only ADMIN can search workers by adminId
                        .requestMatchers(HttpMethod.GET,   "/api/workers/admin")
                        .hasRole("ADMIN")


                        // Only ADMIN can see all users  filter by role
                        .requestMatchers(HttpMethod.GET,   "/api/users")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,   "/api/users/role")
                        .hasRole("ADMIN")

                        // USER can see own profile, ADMIN can see anyone
                        .requestMatchers(HttpMethod.GET,   "/api/users/{id}")
                        .hasAnyRole("USER", "ADMIN")


                        // Only USER can leave feedback
                        .requestMatchers(HttpMethod.POST,  "/api/feedback/create")
                        .hasRole("USER")

                        // All 3 roles can view feedback for a complain
                        .requestMatchers(HttpMethod.GET,   "/api/feedback/complain/**")
                        .hasAnyRole("USER", "WORKER", "ADMIN")

                        // WORKER & ADMIN can see feedback for a worker
                        .requestMatchers(HttpMethod.GET,   "/api/feedback/worker/**")
                        .hasAnyRole("WORKER", "ADMIN")

                        // Only ADMIN sees all feedback
                        .requestMatchers(HttpMethod.GET,   "/api/feedback")
                        .hasRole("ADMIN")


                        // USER uploads image for their complain
                        .requestMatchers(HttpMethod.POST,  "/api/images/upload")
                        .permitAll()

                        // All 3 roles can view images of a complain
                        .requestMatchers(HttpMethod.GET,   "/api/images/complain/**")
                        .hasAnyRole("USER", "WORKER", "ADMIN")

                        // USER can delete own image, ADMIN can delete any
                        .requestMatchers(HttpMethod.DELETE, "/api/images/**")
                        .hasAnyRole("USER", "ADMIN")


                        // WORKER & ADMIN create history entries
                        .requestMatchers(HttpMethod.POST,  "/api/complain-history/create")
                        .hasAnyRole("WORKER", "ADMIN")

                        // All 3 roles can view history of a complain
                        .requestMatchers(HttpMethod.GET,   "/api/complain-history/**")
                        .hasAnyRole("USER", "WORKER", "ADMIN")


                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
