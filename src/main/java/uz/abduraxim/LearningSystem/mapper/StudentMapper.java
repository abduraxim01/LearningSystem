package uz.abduraxim.LearningSystem.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.abduraxim.LearningSystem.DTO.request.UserForRegister;
import uz.abduraxim.LearningSystem.model.Role;
import uz.abduraxim.LearningSystem.model.Student;

@Component
public class StudentMapper {

    final private PasswordEncoder encoder = new BCryptPasswordEncoder();

    public Student toModel(UserForRegister user, String imgUrl) {
        return Student.builder()
                .name(user.getName())
                .password(encoder.encode(user.getPassword()))
                .username(user.getUsername())
                .imageUrl(imgUrl)
                .role(Role.STUDENT)
                .build();
    }
}
