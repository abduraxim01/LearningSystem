package uz.abduraxim.LearningSystem.mapper;

import org.springframework.stereotype.Component;
import uz.abduraxim.LearningSystem.DTO.request.QuestionOptionDTOForRequest;
import uz.abduraxim.LearningSystem.model.Question;
import uz.abduraxim.LearningSystem.model.QuestionOption;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionOptionMapper {

    public QuestionOption toModel(QuestionOptionDTOForRequest dto, Question question) {
        return QuestionOption.builder()
                .content(dto.getContent())
                .isCorrect(dto.isCorrect())
                .question(question)
                .build();
    }

    public List<QuestionOption> toModel(List<QuestionOptionDTOForRequest> dtoList, Question question) {
        if (dtoList == null) return new ArrayList<>();
        return dtoList.stream()
                .map(dto -> toModel(dto, question))
                .toList();
    }
}
