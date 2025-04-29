package uz.abduraxim.LearningSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.abduraxim.LearningSystem.DTO.ResponseStructure;
import uz.abduraxim.LearningSystem.DTO.request.QuestionOptionRequest;
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

    private final ResponseStructure response = new ResponseStructure();

    @Autowired
    public TeacherService(TeacherRepository teacherRep, QuestionRepository questionRep, QuestionOptionMapper questionOptMap, QuestionOptionRepository questionOptRep) {
        this.teacherRep = teacherRep;
        this.questionRep = questionRep;
        this.questionOptMap = questionOptMap;
        this.questionOptRep = questionOptRep;
    }

    public ResponseStructure deleteQuestion(UUID teacherId, UUID questionId) {
        Question question = null;
        try {
            teacherRep.findById(teacherId).orElseThrow(() -> new Exception("Teacher topilmadi"));
            question = questionRep.findById(questionId).orElseThrow(() -> new Exception("Savol topilmadi"));
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Ustoz yoki savol topilmadi");
        }
        if (question.getTeacher().getId().equals(teacherId)) {
            questionRep.delete(question);
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
            response.setMessage("Savol bowqa birovga tegishli");
        }
        return response;
    }

    public ResponseStructure addQuestion(String content, List<QuestionOptionRequest> optionList, UUID teacherId) {
        Teacher teacher;
        try {
            teacher = teacherRep.findById(teacherId).get();
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Ustoz topilmadi");
            return response;
        }
        Subject subject = teacher.getSubject();
        if (questionLimitChecker(teacher.getQuestionList())) {
            if (!questionRep.existsQuestionByContent(content) && optionChecker(optionList)) {
                Question question = questionRep.save(Question.builder()
                        .subject(subject)
                        .teacher(teacher)
                        .content(content)
                        .build());
                questionOptRep.saveAll(questionOptMap.toModel(optionList, question));
                response.setSuccess(true);
            } else {
                response.setSuccess(false);
                response.setMessage("Savol oldindan mavjud yoki Xato variantlar");
            }
        } else {
            response.setSuccess(false);
            response.setMessage("Teacher yangi savol qo'sha olmaydi");
        }
        return response;
    }

    public boolean questionLimitChecker(List<Question> questionList) {
        return questionList.size() < 20;
    }

    public boolean optionChecker(List<QuestionOptionRequest> optionList) {
        return optionList.stream().filter(QuestionOptionRequest::isCorrect).toList().size() == 1;
    }
}
