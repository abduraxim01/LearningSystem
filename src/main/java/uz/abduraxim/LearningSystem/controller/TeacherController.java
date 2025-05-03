package uz.abduraxim.LearningSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.abduraxim.LearningSystem.DTO.request.QuestionRequest;
import uz.abduraxim.LearningSystem.service.TeacherService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/teacher")
public class TeacherController {

    private final TeacherService teacherSer;

    @Autowired
    public TeacherController(TeacherService teacherSer) {
        this.teacherSer = teacherSer;
    }

    @PreAuthorize(value = "hasRole('TEACHER')")
    @PostMapping(value = "/addQuestion")
    public ResponseEntity<?> addQuestion(@RequestBody QuestionRequest request,
                                         Authentication authentication) {
        return ResponseEntity.ok(teacherSer.addQuestion(request.getContent(), request.getOptionList(), authentication));
    }

    @PreAuthorize(value = "hasRole('TEACHER')")
    @PutMapping(value = "/updateQuestion/{questionId}")
    public ResponseEntity<?> updateQuestion(@PathVariable String questionId,
                                            @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(teacherSer.updateQuestion(questionId, request.getContent(), request.getOptionList()));
    }

    @PreAuthorize(value = "hasRole('TEACHER')")
    @DeleteMapping(value = "/deleteQuestion/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable String questionId, Authentication authentication) {
        return ResponseEntity.ok(teacherSer.deleteQuestion(authentication, UUID.fromString(questionId)));
    }

    @PreAuthorize(value = "hasRole('TEACHER')")
    @GetMapping(value = "/getQuestions")
    public ResponseEntity<?> getQuestions(Authentication authentication) {
        return ResponseEntity.ok(teacherSer.getQuestions("", authentication));
    }
}
