package uz.abduraxim.LearningSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uz.abduraxim.LearningSystem.model.Answer;
import uz.abduraxim.LearningSystem.model.Question;
import uz.abduraxim.LearningSystem.model.Student;

import java.util.UUID;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID>, JpaSpecificationExecutor<Answer> {

    boolean existsAnswerByStudentAndQuestion(Student student, Question question);

    Answer findAnswerByStudentAndQuestion(Student student, Question question);
}
