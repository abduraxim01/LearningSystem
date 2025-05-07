package uz.abduraxim.LearningSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import uz.abduraxim.LearningSystem.DTO.response.ResponseStructure;
import uz.abduraxim.LearningSystem.DTO.request.QuestionOptionRequest;
import uz.abduraxim.LearningSystem.mapper.QuestionMapper;
import uz.abduraxim.LearningSystem.mapper.QuestionOptionMapper;
import uz.abduraxim.LearningSystem.model.Question;
import uz.abduraxim.LearningSystem.model.QuestionOption;
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
    private final QuestionMapper questionMap;

    @Autowired
    public TeacherService(TeacherRepository teacherRep, QuestionRepository questionRep, QuestionOptionMapper questionOptMap, QuestionOptionRepository questionOptRep, QuestionMapper questionMap) {
        this.teacherRep = teacherRep;
        this.questionRep = questionRep;
        this.questionOptMap = questionOptMap;
        this.questionOptRep = questionOptRep;
        this.questionMap = questionMap;
    }

    public ResponseStructure getQuestions(String str, Authentication authentication) {
        try {
            UUID id = ((Teacher) authentication.getPrincipal()).getId();
            response.setSuccess(true);
            response.setMessage(str);
            response.setData(questionMap.toDTO(teacherRep.findById(id).get().getSubject().getQuestionList()));
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Qandaydir xatolik");
            response.setData(null);
        }
        return response;
    }

    public ResponseStructure updateQuestion(String questionId, String content, List<QuestionOptionRequest> optionReqList) {
        try {
            Question question = questionRep.findById(UUID.fromString(questionId)).get();
            question.setContent(content);
            questionRep.save(question);
            List<QuestionOption> optionList = question.getOptionList();
            for (int i = 0; i < 4; i++) {
                optionList.get(i).setContent(optionReqList.get(i).getContent());
                optionList.get(i).setCorrect(optionReqList.get(i).isCorrect());
            }
            questionOptRep.saveAll(optionList);
            response.setSuccess(true);
            response.setMessage("Yangilandi");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Savol topilmadi yoki ko'zda tutilmagan xato");
        }
        response.setData(null);
        return response;
    }

    public ResponseStructure deleteQuestion(Authentication authentication, UUID questionId) {
        Question question;
        UUID teacherId;
        try {
            teacherId = ((Teacher) authentication.getPrincipal()).getId();
            teacherRep.findById(teacherId).orElseThrow();
            question = questionRep.findById(questionId).get();
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Ustoz yoki savol topilmadi");
            response.setData(null);
            return response;
        }
        if (question.getTeacher().getId().equals(teacherId)) {
            questionRep.delete(question);
            response.setSuccess(true);
            response.setMessage("");
            response.setData(null);
        } else {
            response.setSuccess(false);
            response.setData(null);
            response.setMessage("Savol bowqa birovga tegishli");
        }
        return response;
    }

    public ResponseStructure addQuestion(String content, List<QuestionOptionRequest> optionList, Authentication authentication) {
        Teacher teacher;
        response.setData(null);
        try {
            UUID teacherId = ((Teacher) authentication.getPrincipal()).getId();
            teacher = teacherRep.findById(teacherId).get();
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Ustoz topilmadi");
            return response;
        }
        Subject subject = teacher.getSubject();
        if (!questionRep.existsQuestionByContent(content) && optionChecker(optionList)) {
            Question question = questionRep.save(Question.builder()
                    .subject(subject)
                    .teacher(teacher)
                    .content(content)
                    .build());
            questionOptRep.saveAll(questionOptMap.toModel(optionList, question));
            response.setSuccess(true);
            response.setMessage("");
        } else {
            response.setSuccess(false);
            response.setMessage("Savol oldindan mavjud yoki Xato variantlar");
        }
        return response;
    }

    public boolean optionChecker(List<QuestionOptionRequest> optionList) {
        return optionList.stream().filter(QuestionOptionRequest::isCorrect).toList().size() == 1;
    }
}
