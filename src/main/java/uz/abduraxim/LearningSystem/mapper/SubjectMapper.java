package uz.abduraxim.LearningSystem.mapper;

import org.springframework.stereotype.Component;
import uz.abduraxim.LearningSystem.DTO.response.SubjectResponse;
import uz.abduraxim.LearningSystem.model.Subject;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubjectMapper {

    public SubjectResponse toDTO(Subject subject) {
        return SubjectResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .build();
    }

    public List<SubjectResponse> toDTO(List<Subject> subjectList) {
        if (subjectList.isEmpty()) return new ArrayList<>();
        return subjectList.stream()
                .map(this::toDTO)
                .toList();
    }
}
