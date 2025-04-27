package uz.abduraxim.LearningSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.abduraxim.LearningSystem.DTO.request.QuestionOptionDTOForRequest;
import uz.abduraxim.LearningSystem.model.Teacher;
import uz.abduraxim.LearningSystem.service.TeacherService;

import java.util.List;
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
    public ResponseEntity<?> addQuestion(@RequestPart("question") String content,
                                         @RequestPart("options") List<QuestionOptionDTOForRequest> optionList,
                                         Authentication authentication) throws Exception {
        UUID teacherId = ((Teacher) authentication.getPrincipal()).getId();
        teacherSer.addQuestion(content, optionList, teacherId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(value = "hasRole('TEACHER')")
    @DeleteMapping(value = "/deleteQuestion/{questionId}")
    public ResponseEntity<?> addQuestion(@PathVariable("questionId") String questionId, Authentication authentication) throws Exception {
        UUID teacherId = ((Teacher) authentication.getPrincipal()).getId();
        teacherSer.deleteQuestion(teacherId, UUID.fromString(questionId));
        return ResponseEntity.ok().build();
    }
}
