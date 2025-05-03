package uz.abduraxim.LearningSystem.DTO.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequest {

    private String content;

    private List<QuestionOptionRequest> optionList;
}
