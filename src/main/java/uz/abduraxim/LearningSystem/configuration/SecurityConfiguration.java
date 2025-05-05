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
                            .requestMatchers("/api/admin/addUser/{subjectId}").hasRole("ADMIN")
                            .requestMatchers("/api/admin/uploadImage").hasRole("ADMIN")
                            .requestMatchers("/api/admin/addSubject").hasRole("ADMIN")
                            .requestMatchers("/api/admin/deleteUser/{username}").hasRole("ADMIN")
                            .requestMatchers("/api/admin/deleteSubject/{subjectId}").hasRole("ADMIN")
                            .requestMatchers("/api/admin/assignTeacherToSubject").hasRole("ADMIN")
                            .requestMatchers("/api/teacher/addQuestion").hasRole("TEACHER")
                            .requestMatchers("/api/teacher/deleteQuestion/{questionId}").hasRole("TEACHER")
                            .requestMatchers("/api/teacher/getQuestions").hasRole("TEACHER")
                            .requestMatchers("/api/teacher/updateQuestion/{questionId}").hasRole("TEACHER")
                            .requestMatchers("/api/admin/getStudentList").hasRole("ADMIN")
                            .requestMatchers("/api/admin/getTeacherList").hasRole("ADMIN")
                            .requestMatchers("/api/admin/updateSubject").hasRole("ADMIN")
                            .requestMatchers("/api/admin/attachSubject").hasRole("ADMIN")
                            .requestMatchers("/api/admin/changeUserDetails").hasRole("ADMIN")
                            .requestMatchers("/api/student/answerToQuestion").hasRole("STUDENT")
                            .requestMatchers("/api/getCurrentUser/{username}").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                            .requestMatchers("/api/student/getQuestions/{subjectId}").hasAnyRole("ADMIN", "STUDENT")
                            .requestMatchers("/api/admin/getSubjectList/{username}").hasAnyRole("ADMIN", "STUDENT", "TEACHER")
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
