package uz.abduraxim.LearningSystem.DTO.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerToQuestion {

    private UUID questionId;

    private UUID optionId;
}
