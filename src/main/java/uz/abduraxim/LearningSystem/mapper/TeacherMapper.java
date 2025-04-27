package uz.abduraxim.LearningSystem.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.abduraxim.LearningSystem.DTO.TeacherResponse;
import uz.abduraxim.LearningSystem.DTO.request.UserForRegister;
import uz.abduraxim.LearningSystem.model.Role;
import uz.abduraxim.LearningSystem.model.Teacher;

@Component
public class TeacherMapper {

    final private PasswordEncoder encoder = new BCryptPasswordEncoder();

    public TeacherResponse toDTO(Teacher teacher) {
        return TeacherResponse.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .imageUrl(teacher.getImageUrl())
                .username(teacher.getUsername())
                .password(teacher.getPassword())
                .build();
    }

    public Teacher toModel(UserForRegister request, String imgUrl) {
        return Teacher.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .imageUrl(imgUrl)
                .role(Role.TEACHER)
                .build();
    }
}
