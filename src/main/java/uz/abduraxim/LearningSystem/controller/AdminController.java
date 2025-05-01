package uz.abduraxim.LearningSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.abduraxim.LearningSystem.DTO.request.UserForRegister;
import uz.abduraxim.LearningSystem.service.AdminService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {

    private final AdminService adminSer;

    @Autowired
    public AdminController(AdminService adminSer) {
        this.adminSer = adminSer;
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PutMapping(value = "/changeUserDetails/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> changeUserDetails(@PathVariable("userId") String userId,
                                               @RequestPart("newName") String newName,
                                               @RequestPart("newUsername") String newUsername,
                                               @RequestPart("newPassword") String newPassword,
                                               @RequestPart("newImage") MultipartFile file,
                                               @RequestPart("student") String isStudent) {
        return ResponseEntity.ok(adminSer.updateUserDetails(userId, newName, newUsername, newPassword, file, isStudent));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PutMapping(value = "/attachSubject")
    public ResponseEntity<?> attachSubject(@RequestPart("user") String userId,
                                           @RequestPart("subject") String subjectId) {

        return ResponseEntity.ok(adminSer.attachSubject(userId, subjectId));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/addUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addUser(@RequestPart("user") UserForRegister request,
                                     @RequestPart("subject") String subjectId,
                                     @RequestPart("image") MultipartFile file) {
        UUID uuid = UUID.fromString(subjectId);
        return ResponseEntity.ok(adminSer.addUser(request, file, uuid));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @DeleteMapping(value = "/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestPart("student") String isStudent,
                                        @RequestPart("id") String userId) {
        return ResponseEntity.ok(adminSer.deleteUser(userId, isStudent));
    }


    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/addSubject/{subjectName}")
    public ResponseEntity<?> addSubject(@PathVariable(name = "subjectName") String subjectName) {
        return ResponseEntity.ok(adminSer.addSubject(subjectName));
    }

    @PreAuthorize(value = "hasRole('TEACHER')")
    @DeleteMapping(value = "/deleteSubject/{subjectId}")
    public ResponseEntity<?> deleteSubject(@PathVariable("subjectId") String subjectId) {
        return ResponseEntity.ok(adminSer.deleteSubject(subjectId));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PutMapping(value = "/updateSubject")
    public ResponseEntity<?> updateSubject(@RequestPart("name") String newName,
                                           @RequestPart("id") String subjectId) {
        return ResponseEntity.ok(adminSer.updateSubject(subjectId, newName));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping(value = "/getTeacherList")
    public ResponseEntity<?> getTeacherList() {
        return ResponseEntity.ok(adminSer.getAllTeachers());
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping(value = "/getStudentList")
    public ResponseEntity<?> getStudentList() {
        return ResponseEntity.ok(adminSer.getAllStudents());
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    @GetMapping(value = "/getSubjectList")
    public ResponseEntity<?> getSubjectList(@RequestPart("student") String isStudent,
                                            Authentication authentication) {
        return ResponseEntity.ok(adminSer.getSubjectList(authentication, isStudent));
    }
}
