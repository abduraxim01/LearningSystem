package uz.abduraxim.LearningSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.abduraxim.LearningSystem.model.Answer;
import uz.abduraxim.LearningSystem.model.Question;
import uz.abduraxim.LearningSystem.model.Student;
import uz.abduraxim.LearningSystem.repository.AnswerRepository;
import uz.abduraxim.LearningSystem.repository.QuestionOptionRepository;
import uz.abduraxim.LearningSystem.repository.QuestionRepository;
import uz.abduraxim.LearningSystem.repository.StudentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final QuestionRepository questionRep;

    private final AnswerRepository answerRep;

    private final StudentRepository studentRep;

    private final QuestionOptionRepository questionOptionRep;

    @Autowired
    public StudentService(QuestionRepository questionRep, AnswerRepository answerRep, StudentRepository studentRep, QuestionOptionRepository questionOptionRep) {
        this.questionRep = questionRep;
        this.answerRep = answerRep;
        this.studentRep = studentRep;
        this.questionOptionRep = questionOptionRep;
    }

    public boolean answerToQuestion(UUID studentId, UUID questionId, UUID optionId) throws Exception {
        Student student = studentRep.findById(studentId).orElseThrow(() -> new Exception("Student topilmadi"));
        Question question = questionRep.findById(questionId).orElseThrow(() -> new Exception("Savol topilmadi"));
        List<Answer> studentAnswers = student.getAnswerList();
        if (studentAnswers.stream().noneMatch(answer -> answer.getQuestion().getId().equals(questionId))) {
            if (question.getOptionList().stream().anyMatch(option -> option.getId().equals(optionId))) {
                boolean isCorrect = questionOptionRep.findById(optionId).get().isCorrect();
                answerRep.save(Answer.builder()
                        .student(student)
                        .question(question)
                        .isCorrect(isCorrect)
                        .build());
                return isCorrect;
            } else throw new Exception("Bowqa savol varianti");
        } else throw new Exception("Siz oldin javob bergansiz");
    }
}
