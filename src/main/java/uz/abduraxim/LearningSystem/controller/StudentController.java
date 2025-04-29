package uz.abduraxim.LearningSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.abduraxim.LearningSystem.DTO.request.AnswerToQuestion;
import uz.abduraxim.LearningSystem.model.Student;
import uz.abduraxim.LearningSystem.service.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/student")
public class StudentController {

    private final StudentService studentSer;

    @Autowired
    public StudentController(StudentService studentSer) {
        this.studentSer = studentSer;
    }

    @PreAuthorize(value = "hasRole('STUDENT')")
    @PostMapping(value = "/answerToQuestion")
    public ResponseEntity<?> answerToQuestion(@RequestPart("answers") List<AnswerToQuestion> answerList,
                                              Authentication authentication) throws Exception {
        UUID studentId = ((Student) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(studentSer.answerToQuestion(studentId, answerList));
    }

    @PreAuthorize(value = "hasAnyRole('STUDENT','TEACHER','ADMIN')")
    @GetMapping(value = "/getQuestions")
    public ResponseEntity<?> getQuestions(@RequestPart("subject") String subjectId) {
        return ResponseEntity.ok(studentSer.getQuestions(subjectId));
    }

    // must add
    @PreAuthorize(value = "hasRole('STUDENT')")
//    @PostMapping(value = "/changeStudentDetails")
    public ResponseEntity<?> changeStudentDetails(@RequestPart("newName") String newName,
                                                  @RequestPart("newUsername") String newUsername,
                                                  @RequestPart("newImage") String file,
                                                  Authentication authentication) {
        UUID studentId = ((Student) authentication.getPrincipal()).getId();
        return null;
    }
}
