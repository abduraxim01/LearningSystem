package uz.abduraxim.LearningSystem.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.abduraxim.LearningSystem.DTO.request.UserForChangeDetails;
import uz.abduraxim.LearningSystem.DTO.request.UserForRegister;
import uz.abduraxim.LearningSystem.DTO.response.UserResponse;
import uz.abduraxim.LearningSystem.model.Role;
import uz.abduraxim.LearningSystem.model.Student;
import uz.abduraxim.LearningSystem.model.Subject;

import java.util.ArrayList;
import java.util.List;

@Component
public class StudentMapper {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public Student toModel(Student student, UserForChangeDetails user) {
        student.setName(user.getNewName());
        student.setUsername(user.getNewUsername());
        student.setPassword(encoder.encode(user.getNewPassword()));
        if (user.getImgUrl() != null) student.setImageUrl(user.getImgUrl());
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
