package uz.abduraxim.LearningSystem.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.abduraxim.LearningSystem.DTO.response.QuestionResponse;
import uz.abduraxim.LearningSystem.model.Question;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionMapper {

    private final QuestionOptionMapper questionOptionMapper;

    @Autowired
    public QuestionMapper(QuestionOptionMapper questionOptionMapper) {
        this.questionOptionMapper = questionOptionMapper;
    }

    public QuestionResponse toDTO(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .content(question.getContent())
                .optionList(questionOptionMapper.toDTO(question.getOptionList()))
                .build();
    }

    public List<QuestionResponse> toDTO(List<Question> questionList) {
        if (questionList == null) return new ArrayList<>();
        return questionList.stream()
                .map(this::toDTO)
                .toList();
    }
}
