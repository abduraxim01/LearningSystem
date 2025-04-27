package uz.abduraxim.LearningSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import uz.abduraxim.LearningSystem.model.Student;
import uz.abduraxim.LearningSystem.service.StudentService;

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
    public ResponseEntity<?> answerToQuestion(@RequestPart("question") String questionId,
                                              @RequestPart("option") String optionId,
                                              Authentication authentication) throws Exception {
        UUID studentId = ((Student) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(studentSer.answerToQuestion(studentId,
                UUID.fromString(questionId), UUID.fromString(optionId)));
    }
}
