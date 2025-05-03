package uz.abduraxim.LearningSystem.DTO.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachSubject {

    public String studentId;

    public List<String> subjectIds;
}
