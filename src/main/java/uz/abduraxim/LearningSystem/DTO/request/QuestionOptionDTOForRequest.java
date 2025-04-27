package uz.abduraxim.LearningSystem.DTO.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOptionDTOForRequest {

    private String content;

    private boolean isCorrect;
}
