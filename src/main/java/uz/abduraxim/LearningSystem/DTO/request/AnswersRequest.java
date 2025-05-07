package uz.abduraxim.LearningSystem.DTO.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswersRequest {

    private String subjectId;

    private String studentId;
}
