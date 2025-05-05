package uz.abduraxim.LearningSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.abduraxim.LearningSystem.model.Subject;

import java.util.UUID;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    boolean existsSubjectByName(String name);
}
