package uz.abduraxim.LearningSystem.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.abduraxim.LearningSystem.DTO.response.UserResponse;
import uz.abduraxim.LearningSystem.DTO.request.UserForRegister;
import uz.abduraxim.LearningSystem.model.Role;
import uz.abduraxim.LearningSystem.model.Student;
import uz.abduraxim.LearningSystem.model.Subject;

import java.util.ArrayList;
import java.util.List;

@Component
public class StudentMapper {

    final private PasswordEncoder encoder = new BCryptPasswordEncoder();

    public Student toModel(Student student, String newName, String newUsername, String newPassword, String imgUrl) {
        student.setName(newName);
        student.setUsername(newUsername);
        student.setPassword(encoder.encode(newPassword));
        if (imgUrl != null) student.setImageUrl(imgUrl);
        return student;
    }

    public Student toModel(UserForRegister user, Subject subject) {
        return Student.builder()
                .name(user.getName())
                .password(encoder.encode(user.getPassword()))
                .username(user.getUsername())
                .subject(List.of(subject))
                .imageUrl(user.getImgUrl())
                .role(Role.STUDENT)
                .build();
    }

    public UserResponse toDTO(Student student) {
        return UserResponse.builder()
                .id(student.getId())
                .name(student.getName())
                .username(student.getUsername())
                .imageUrl(student.getImageUrl())
                .build();
    }

    public List<UserResponse> toDTO(List<Student> students) {
        if (students == null || students.isEmpty()) return new ArrayList<>();
        return students.stream()
                .map(this::toDTO)
                .toList();
    }
}
