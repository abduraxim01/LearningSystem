package uz.abduraxim.LearningSystem.DTO.response;

import jakarta.persistence.Column;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {

    private UUID id;

    private String content;

    private List<QuestionOptionResponse> optionList;
}
