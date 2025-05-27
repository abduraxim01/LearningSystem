package uz.abduraxim.LearningSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.abduraxim.LearningSystem.DTO.request.*;
import uz.abduraxim.LearningSystem.model.Role;
import uz.abduraxim.LearningSystem.service.AdminService;
import uz.abduraxim.LearningSystem.service.ImageService;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {

    private final AdminService adminSer;

    private final ImageService imageSer;

    @Autowired
    public AdminController(AdminService adminSer, ImageService imageSer) {
        this.adminSer = adminSer;
        this.imageSer = imageSer;
    }

    @PreAuthorize(value = "hasRole('SUPERADMIN')")
    @PostMapping(value = "/addAdmin")
    public ResponseEntity<?> addAdmin(@RequestBody UserForRegister register) {
        return ResponseEntity.ok(adminSer.addAdmin(register));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERADMIN')")
    @PutMapping(value = "/changeUserDetails")
    public ResponseEntity<?> changeUserDetails(@RequestBody UserForChangeDetails details) {
        return ResponseEntity.ok(adminSer.updateUserDetails(details));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERADMIN')")
    @PutMapping(value = "/attachSubject")
    public ResponseEntity<?> attachSubject(@RequestBody AttachSubject attachSubject) {
        return ResponseEntity.ok(adminSer.attachSubject(attachSubject));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping(value = "/addUser/{subjectId}")
    public ResponseEntity<?> addUser(@RequestBody UserForRegister request,
                                     @PathVariable String subjectId) {
        return ResponseEntity.ok(adminSer.addUser(request, subjectId));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile file) throws Exception {
        return ResponseEntity.ok(imageSer.uploadImage(file));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping(value = "/deleteUser/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        return ResponseEntity.ok(adminSer.deleteUser(username));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping(value = "/addSubject")
    public ResponseEntity<?> addSubject(@RequestBody SubjectRequest subject) {
        return ResponseEntity.ok(adminSer.addSubject(subject.getName()));
    }

    @PreAuthorize(value = "hasRole('TEACHER')")
    @DeleteMapping(value = "/deleteSubject/{subjectId}")
    public ResponseEntity<?> deleteSubject(@PathVariable String subjectId) {
        return ResponseEntity.ok(adminSer.deleteSubject(subjectId));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERADMIN')")
    @PutMapping(value = "/updateSubject")
    public ResponseEntity<?> updateSubject(@RequestBody SubjectRequest subject) {
        return ResponseEntity.ok(adminSer.updateSubject(subject.getId(), subject.getName()));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERADMIN')")
    @GetMapping(value = "/getTeacherList")
    public ResponseEntity<?> getTeacherList() {
        return ResponseEntity.ok(adminSer.getAdminOrAllTeacherList(Role.TEACHER));
    }

    @PreAuthorize(value = "hasRole('SUPERADMIN')")
    @GetMapping(value = "/getAdminList")
    public ResponseEntity<?> getAdminList() {
        return ResponseEntity.ok(adminSer.getAdminOrAllTeacherList(Role.ADMIN));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERADMIN')")
    @GetMapping(value = "/getStudentList")
    public ResponseEntity<?> getStudentList() {
        return ResponseEntity.ok(adminSer.getAllStudents());
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','TEACHER', 'STUDENT','SUPERADMIN')")
    @GetMapping(value = "/getSubjectList/{username}")
    public ResponseEntity<?> getSubjectList(@PathVariable String username) {
        return ResponseEntity.ok(adminSer.getSubjectList(username));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','TEACHER','STUDENT','SUPERADMIN')")
    @GetMapping(value = "/getAnswers")
    public ResponseEntity<?> getAnswers(@RequestBody AnswersRequest request,
                                        Authentication authentication) {
        return ResponseEntity.ok(adminSer.getCorrectAnswerCounts(authentication, request));
    }
}
