package uz.abduraxim.LearningSystem.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.abduraxim.LearningSystem.service.auth.AuthService;
import uz.abduraxim.LearningSystem.service.jwtService.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthService authService;

    private final JwtFilter jwtFilter;

    final private String[] WHITE_LIST = {
            "/api/v1/auth/**", "/api/v1/auth",
            "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**",
            "/api/login"};

    @Autowired
    public SecurityConfiguration(AuthService authService, JwtFilter jwtFilter) {
        this.authService = authService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requestConfigurer -> {
                    requestConfigurer
                            .requestMatchers(WHITE_LIST).permitAll()
                            .requestMatchers("/api/admin/addUser").hasRole("ADMIN")
                            .requestMatchers("/api/admin/addSubject/{subjectname}").hasRole("ADMIN")
                            .requestMatchers("/api/admin/assignTeacherToSubject").hasRole("ADMIN")
                            .requestMatchers("/api/teacher/addQuestion").hasRole("TEACHER")
                            .requestMatchers("/api/teacher/deleteQuestion/{questionId}").hasRole("TEACHER")
                            .requestMatchers("/api/student/answerToQuestion").hasRole("STUDENT")
                            .anyRequest().authenticated();
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(authService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
