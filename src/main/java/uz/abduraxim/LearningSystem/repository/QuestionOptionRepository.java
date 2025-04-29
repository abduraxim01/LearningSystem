package uz.abduraxim.LearningSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.abduraxim.LearningSystem.model.Question;
import uz.abduraxim.LearningSystem.model.QuestionOption;

import java.util.UUID;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, UUID> {

    QuestionOption findByQuestion(Question question);
}
