package uz.abduraxim.LearningSystem.mapper;

import org.springframework.stereotype.Component;
import uz.abduraxim.LearningSystem.DTO.request.QuestionOptionRequest;
import uz.abduraxim.LearningSystem.DTO.response.QuestionOptionResponse;
import uz.abduraxim.LearningSystem.model.Question;
import uz.abduraxim.LearningSystem.model.QuestionOption;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionOptionMapper {

    public QuestionOption toModel(QuestionOptionRequest dto, Question question) {
        return QuestionOption.builder()
                .content(dto.getContent())
                .isCorrect(dto.isCorrect())
                .question(question)
                .build();
    }

    public List<QuestionOption> toModel(List<QuestionOptionRequest> dtoList, Question question) {
        if (dtoList == null) return new ArrayList<>();
        return dtoList.stream()
                .map(dto -> toModel(dto, question))
                .toList();
    }

    public QuestionOptionResponse toDTO(QuestionOption option) {
        return QuestionOptionResponse.builder()
                .id(option.getId())
                .content(option.getContent())
                .build();
    }

    public List<QuestionOptionResponse> toDTO(List<QuestionOption> optionList) {
        if (optionList.isEmpty()) return new ArrayList<>();
        return optionList.stream()
                .map(this::toDTO)
                .toList();
    }
}
