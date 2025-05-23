package uz.abduraxim.LearningSystem.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.abduraxim.LearningSystem.DTO.response.UserResponse;
import uz.abduraxim.LearningSystem.DTO.request.UserForRegister;
import uz.abduraxim.LearningSystem.model.Role;
import uz.abduraxim.LearningSystem.model.Subject;
import uz.abduraxim.LearningSystem.model.Teacher;

import java.util.ArrayList;
import java.util.List;

@Component
public class TeacherMapper {

    final private PasswordEncoder encoder = new BCryptPasswordEncoder();

    public Teacher toModel(Teacher teacher, String newName, String newUsername, String newPassword, String imgUrl) {
        teacher.setName(newName);
        teacher.setUsername(newUsername);
        teacher.setPassword(encoder.encode(newPassword));
        if (imgUrl != null) teacher.setImageUrl(imgUrl);
        return teacher;
    }

    public UserResponse toDTO(Teacher teacher) {
        return UserResponse.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .imageUrl(teacher.getImageUrl())
                .username(teacher.getUsername())
                .build();
    }

    public List<UserResponse> toDTO(List<Teacher> teachers) {
        if (teachers == null) return new ArrayList<>();
        return teachers.stream()
                .map(this::toDTO)
                .toList();
    }

    public Teacher toModel(UserForRegister request, Subject subject) {
        return Teacher.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .subject(subject)
                .imageUrl(request.getImgUrl())
                .role(Role.TEACHER)
                .build();
    }
}
