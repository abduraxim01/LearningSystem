package uz.abduraxim.LearningSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.abduraxim.LearningSystem.DTO.request.AttachSubject;
import uz.abduraxim.LearningSystem.DTO.request.SubjectRequest;
import uz.abduraxim.LearningSystem.DTO.request.UserForChangeDetails;
import uz.abduraxim.LearningSystem.DTO.request.UserForRegister;
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

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PutMapping(value = "/changeUserDetails")
    public ResponseEntity<?> changeUserDetails(@RequestBody UserForChangeDetails details) {
        return ResponseEntity.ok(adminSer.updateUserDetails(details));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PutMapping(value = "/attachSubject")
    public ResponseEntity<?> attachSubject(@RequestBody AttachSubject attachSubject) {
        return ResponseEntity.ok(adminSer.attachSubject(attachSubject));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/addUser/{subjectId}")
    public ResponseEntity<?> addUser(@RequestBody UserForRegister request,
                                     @PathVariable String subjectId) {
        return ResponseEntity.ok(adminSer.addUser(request, subjectId));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile file) throws Exception {
        return ResponseEntity.ok(imageSer.uploadImage(file));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @DeleteMapping(value = "/deleteUser/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        return ResponseEntity.ok(adminSer.deleteUser(username));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/addSubject")
    public ResponseEntity<?> addSubject(@RequestBody SubjectRequest subject) {
        return ResponseEntity.ok(adminSer.addSubject(subject.getName()));
    }

    @PreAuthorize(value = "hasRole('TEACHER')")
    @DeleteMapping(value = "/deleteSubject/{subjectId}")
    public ResponseEntity<?> deleteSubject(@PathVariable String subjectId) {
        return ResponseEntity.ok(adminSer.deleteSubject(subjectId));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PutMapping(value = "/updateSubject")
    public ResponseEntity<?> updateSubject(@RequestBody SubjectRequest subject) {
        return ResponseEntity.ok(adminSer.updateSubject(subject.getId(), subject.getName()));
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

    @PreAuthorize(value = "hasAnyRole('ADMIN','TEACHER', 'STUDENT')")
    @GetMapping(value = "/getSubjectList/{username}")
    public ResponseEntity<?> getSubjectList(@PathVariable String username) {
        return ResponseEntity.ok(adminSer.getSubjectList(username));
    }

    // must add
//    @PreAuthorize(value = "hasRole('ADMIN')")
//    @GetMapping(value = "/getAnswers/{username}")
//    public ResponseEntity<?> getAnswers(@PathVariable String username){
//
//    }
}
