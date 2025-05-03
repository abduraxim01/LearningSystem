package uz.abduraxim.LearningSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.abduraxim.LearningSystem.model.Teacher;

import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    void deleteTeacherByUsername(String username);

    boolean existsTeacherByUsername(String username);

    Teacher findTeacherByUsername(String username);
}
