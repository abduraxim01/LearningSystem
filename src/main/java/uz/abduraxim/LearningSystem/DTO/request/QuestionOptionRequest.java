package uz.abduraxim.LearningSystem.DTO.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOptionRequest {

    private String content;

    private boolean isCorrect;
}
