package uz.abduraxim.LearningSystem.DTO.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswersResponse {

    private UUID studentId;

    private String studentName;

    private UUID subjectId;

    private String subjectName;

    private long count;
}
