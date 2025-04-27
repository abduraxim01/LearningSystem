package uz.abduraxim.LearningSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PostMapping(value = "/addUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addUser(@RequestPart("user") UserForRegister request,
                                     @RequestPart("image") MultipartFile file) throws Exception {
        adminSer.addUser(request, file);
        return ResponseEntity.ok("Received");
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/addSubject/{subjectName}")
    public ResponseEntity<?> addSubject(@PathVariable String subjectName) throws Exception {
        return ResponseEntity.ok(adminSer.addSubject(subjectName));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/assignTeacherToSubject")
    public ResponseEntity<?> assignTeacherToSubject(@RequestPart("teacher") String teacherId,
                                                    @RequestPart("subject") String subjectId) throws Exception {
        adminSer.assignTeacherToSubject(UUID.fromString(teacherId), UUID.fromString(subjectId));
        return ResponseEntity.ok().build();
    }
}
