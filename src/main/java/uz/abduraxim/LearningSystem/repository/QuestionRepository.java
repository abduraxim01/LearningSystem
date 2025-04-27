package uz.abduraxim.LearningSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.abduraxim.LearningSystem.model.Question;

import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    boolean existsQuestionByContent(String content);
}
