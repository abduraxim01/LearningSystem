package uz.abduraxim.LearningSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import uz.abduraxim.LearningSystem.DTO.response.ResponseStructure;
import uz.abduraxim.LearningSystem.DTO.request.AnswerToQuestion;
import uz.abduraxim.LearningSystem.DTO.response.QuestionResponse;
import uz.abduraxim.LearningSystem.mapper.QuestionMapper;
import uz.abduraxim.LearningSystem.model.Answer;
import uz.abduraxim.LearningSystem.model.Question;
import uz.abduraxim.LearningSystem.model.Student;
import uz.abduraxim.LearningSystem.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final QuestionRepository questionRep;

    private final AnswerRepository answerRep;

    private final StudentRepository studentRep;

    private final QuestionOptionRepository questionOptionRep;

    private final SubjectRepository subjectRep;

    private final QuestionMapper questionMapper;

    private final ResponseStructure response = new ResponseStructure();

    @Autowired
    public StudentService(QuestionRepository questionRep, AnswerRepository answerRep, StudentRepository studentRep, QuestionOptionRepository questionOptionRep, SubjectRepository subjectRep, QuestionMapper questionMapper) {
        this.questionRep = questionRep;
        this.answerRep = answerRep;
        this.studentRep = studentRep;
        this.questionOptionRep = questionOptionRep;
        this.subjectRep = subjectRep;
        this.questionMapper = questionMapper;
    }

    public ResponseStructure getQuestions(String subjectId) {
        try {
            List<Question> questionList = subjectRep.findById(UUID.fromString(subjectId)).get().getQuestionList();
            Collections.shuffle(questionList);
            List<QuestionResponse> responseList = questionMapper.toDTO(questionList.stream().limit(20).toList());
            if (responseList.size() < 20) {
                response.setSuccess(false);
                response.setMessage("Testlar soni kam");
                response.setData(null);
            } else {
                response.setSuccess(true);
                response.setData(responseList);
                response.setMessage("");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Fan topilmadi");
            response.setData(null);
        }
        return response;
    }

    public ResponseStructure answerToQuestion(Authentication authentication, List<AnswerToQuestion> answerList) {
        long count;
        try {
            count = answerList.stream()
                    .filter(ans -> questionOptionRep.findById(ans.getOptionId()).get().isCorrect())
                    .count();
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Variant topilmadi");
            return response;
        }
        try {
            UUID studentId = ((Student) authentication.getPrincipal()).getId();
            List<Answer> answers = answerList.stream()
                    .map(ans -> Answer.builder()
                            .question(questionRep.findById(ans.getQuestionId()).get())
                            .isCorrect(questionOptionRep.findById(ans.getOptionId()).get().isCorrect())
                            .student(studentRep.findById(studentId).get())
                            .build())
                    .toList();
            answerRep.saveAll(answers);
            response.setSuccess(true);
            response.setData(count);
            response.setMessage("");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Savol topilmadi yoki qandaydir xatolik");
            response.setData(null);
        }
        return response;
    }
}
