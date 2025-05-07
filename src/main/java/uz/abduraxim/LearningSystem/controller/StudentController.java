package uz.abduraxim.LearningSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.abduraxim.LearningSystem.DTO.request.AnswerToQuestion;
import uz.abduraxim.LearningSystem.service.StudentService;

import java.util.List;

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
    public ResponseEntity<?> answerToQuestion(@RequestBody List<AnswerToQuestion> answerList,
                                              Authentication authentication) {
        return ResponseEntity.ok(studentSer.answerToQuestion(authentication, answerList));
    }

    @PreAuthorize(value = "hasAnyRole('STUDENT','ADMIN')")
    @GetMapping(value = "/getQuestions/{subjectId}")
    public ResponseEntity<?> getQuestions(@PathVariable String subjectId) {
        return ResponseEntity.ok(studentSer.getQuestions(subjectId));
    }
}
