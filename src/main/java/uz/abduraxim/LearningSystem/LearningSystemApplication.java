package uz.abduraxim.LearningSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.abduraxim.LearningSystem.model.Role;
import uz.abduraxim.LearningSystem.model.Teacher;
import uz.abduraxim.LearningSystem.repository.TeacherRepository;

@SpringBootApplication
public class LearningSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningSystemApplication.class, args);

    }

    @Autowired
    private TeacherRepository teacherRep;

    final private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Bean
    public CommandLineRunner startUp() {
        return args -> {
            if (!teacherRep.existsTeacherByUsername("admin")) {
                teacherRep.save(Teacher.builder()
                        .name("Admin")
                        .username("admin")
                        .password(encoder.encode("admin"))
                        .role(Role.ADMIN)
                        .build());
            }
        };
    }
}
