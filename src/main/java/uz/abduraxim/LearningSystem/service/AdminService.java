package uz.abduraxim.LearningSystem.service;

import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.abduraxim.LearningSystem.DTO.ResponseStructure;
import uz.abduraxim.LearningSystem.DTO.request.UserForRegister;
import uz.abduraxim.LearningSystem.mapper.StudentMapper;
import uz.abduraxim.LearningSystem.mapper.SubjectMapper;
import uz.abduraxim.LearningSystem.mapper.TeacherMapper;
import uz.abduraxim.LearningSystem.model.Student;
import uz.abduraxim.LearningSystem.model.Subject;
import uz.abduraxim.LearningSystem.model.Teacher;
import uz.abduraxim.LearningSystem.repository.StudentRepository;
import uz.abduraxim.LearningSystem.repository.SubjectRepository;
import uz.abduraxim.LearningSystem.repository.TeacherRepository;

import java.util.UUID;

@Service
public class AdminService {

    private final TeacherRepository teacherRep;

    private final StudentRepository studentRep;

    private final Logger log = LogManager.getLogger(AdminService.class);

    private final TeacherMapper teacherMap;

    private final StudentMapper studentMap;

    private final SubjectRepository subjectRep;

    private final ImageService imageSer;

    private final SubjectMapper subjectMap;

    private final ResponseStructure response = new ResponseStructure();

    @Autowired
    public AdminService(TeacherRepository teacherRep, StudentRepository studentRep, TeacherMapper teacherMap, StudentMapper studentMap, SubjectRepository subjectRep, ImageService imageSer, SubjectMapper subjectMap) {
        this.teacherRep = teacherRep;
        this.studentRep = studentRep;
        this.teacherMap = teacherMap;
        this.studentMap = studentMap;
        this.subjectRep = subjectRep;
        this.imageSer = imageSer;
        this.subjectMap = subjectMap;
    }

    public ResponseStructure getSubjectList(Authentication authentication, String isStudent) {
        ResponseStructure response = new ResponseStructure();
        UUID userId;
        if (isStudent.equals("true")) {
            userId = ((Student) authentication.getPrincipal()).getId();
            Student student = studentRep.findById(userId).get();
            response.setSuccess(true);
            response.setData(subjectMap.toDTO(student.getSubject()));
            System.out.println(response.toString());
        } else if (isStudent.equals("false")) {
            userId = ((Teacher) authentication.getPrincipal()).getId();
            Teacher teacher = teacherRep.findById(userId).get();
            response.setSuccess(true);
            response.setData(subjectMap.toDTO(teacher.getSubject()));
        }
        return response;
    }

    public ResponseStructure getAllTeachers() {
        response.setSuccess(true);
        response.setData(teacherMap.toDTO(teacherRep.findAll()));
        return response;
    }

    public ResponseStructure getAllStudents() {
        response.setSuccess(true);
        response.setData(studentMap.toDTO(studentRep.findAll()));
        return response;
    }

    public ResponseStructure addSubject(String name) {
        if (!subjectRep.existsSubjectByName(name)) {
            response.setSuccess(true);
            response.setData(subjectRep.save(Subject.builder()
                    .name(name)
                    .build()).getId());
        } else {
            response.setSuccess(false);
            response.setMessage("Fan oldindan mavjud");
        }
        return response;
    }

    public ResponseStructure addUser(UserForRegister request, MultipartFile file, UUID subjectId) {
        Subject subject;
        try {
            subject = subjectRep.findById(subjectId).orElseThrow();
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Fan topilmadi");
            return response;
        }
        if (isHave(request.getUsername())) {
            response.setSuccess(false);
            response.setMessage("Username oldindan mavjud");
            return response;
        } else {
            String imgUrl = "";
            if (!file.isEmpty()) {
                try {
                    imgUrl = imageSer.uploadImage(file);
                } catch (Exception e) {
                    response.setSuccess(false);
                    response.setMessage("Rasm yuklashda xatolik");
                    return response;
                }
            }
            try {
                if (request.isStudent()) studentRep.save(studentMap.toModel(request, subject, imgUrl));
                else teacherRep.save(teacherMap.toModel(request, subject, imgUrl));
                log.info("Saqlandi username: {}", request.getUsername());
                response.setSuccess(true);
                response.setMessage("saqlandi");
                return response;
            } catch (ConstraintViolationException e) {
                response.setSuccess(false);
                response.setMessage("Ko'zda tutilmagan xatolik");
                return response;
            }
        }
    }

    public boolean isHave(String username) {
        return teacherRep.existsTeacherByUsername(username) || studentRep.existsStudentByUsername(username);
    }
}
