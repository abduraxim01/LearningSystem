package uz.abduraxim.LearningSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.abduraxim.LearningSystem.DTO.request.QuestionOptionDTOForRequest;
import uz.abduraxim.LearningSystem.mapper.QuestionOptionMapper;
import uz.abduraxim.LearningSystem.model.Question;
import uz.abduraxim.LearningSystem.model.Subject;
import uz.abduraxim.LearningSystem.model.Teacher;
import uz.abduraxim.LearningSystem.repository.QuestionOptionRepository;
import uz.abduraxim.LearningSystem.repository.QuestionRepository;
import uz.abduraxim.LearningSystem.repository.TeacherRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TeacherService {

    private final TeacherRepository teacherRep;
    private final QuestionRepository questionRep;

    private final QuestionOptionMapper questionOptMap;
    private final QuestionOptionRepository questionOptRep;

    @Autowired
    public TeacherService(TeacherRepository teacherRep, QuestionRepository questionRep, QuestionOptionMapper questionOptMap, QuestionOptionRepository questionOptRep) {
        this.teacherRep = teacherRep;
        this.questionRep = questionRep;
        this.questionOptMap = questionOptMap;
        this.questionOptRep = questionOptRep;
    }

    public void deleteQuestion(UUID teacherId, UUID questionId) throws Exception {
        teacherRep.findById(teacherId).orElseThrow(() -> new Exception("Teacher topilmadi"));
        Question question = questionRep.findById(questionId).orElseThrow(() -> new Exception("Savol topilmadi"));
        if (question.getTeacher().getId().equals(teacherId)) {
            questionRep.delete(question);
        } else throw new Exception();
    }

    public void addQuestion(String content, List<QuestionOptionDTOForRequest> optionList, UUID teacherId) throws Exception {
        Teacher teacher = teacherRep.findById(teacherId).orElseThrow(() -> new Exception("Teacher topilmadi"));
        Subject subject = teacher.getSubject();
        if (questionLimitChecker(teacher.getQuestionList())) {
            if (!questionRep.existsQuestionByContent(content) && optionChecker(optionList)) {
                Question question = questionRep.save(Question.builder()
                        .subject(subject)
                        .teacher(teacher)
                        .content(content)
                        .build());
                questionOptRep.saveAll(questionOptMap.toModel(optionList, question));
            } else throw new Exception("Savol oldindan mavjud yoki Xato variantlar");
        } else throw new Exception("Teacher yangi savol qo'sha olmaydi yoki Biror fanga biriltirilmagan");
    }

    public boolean questionLimitChecker(List<Question> questionList) {
        return questionList.size() < 20;
    }

    public boolean optionChecker(List<QuestionOptionDTOForRequest> optionList) {
        return optionList.stream().filter(QuestionOptionDTOForRequest::isCorrect).toList().size() == 1;
    }
}
