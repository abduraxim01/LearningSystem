package uz.abduraxim.LearningSystem.service;

import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.abduraxim.LearningSystem.DTO.request.UserForRegister;
import uz.abduraxim.LearningSystem.mapper.StudentMapper;
import uz.abduraxim.LearningSystem.mapper.TeacherMapper;
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

    @Autowired
    public AdminService(TeacherRepository teacherRep, StudentRepository studentRep, TeacherMapper teacherMap, StudentMapper studentMap, SubjectRepository subjectRep) {
        this.teacherRep = teacherRep;
        this.studentRep = studentRep;
        this.teacherMap = teacherMap;
        this.studentMap = studentMap;
        this.subjectRep = subjectRep;
    }

    public void assignTeacherToSubject(UUID teacherId, UUID subjectId) throws Exception {
        Subject subject = subjectRep.findById(subjectId).orElseThrow(() -> new Exception("Subject topilmadi"));
        Teacher teacher = teacherRep.findById(teacherId).orElseThrow(() -> new Exception("Teacher topilmadi"));
        if (subject != null && teacher != null && teacher.getSubject() == null) {
            teacher.setSubject(subject);
            teacherRep.save(teacher);
        } else throw new Exception("Oldindan mavjud");
    }

    public UUID addSubject(String name) throws Exception {
        if (!subjectRep.existsSubjectByName(name)) {
            return subjectRep.save(Subject.builder()
                    .name(name)
                    .build()).getId();
        } else throw new Exception("Fan oldindan mavjud");
    }

    public void addUser(UserForRegister request, MultipartFile file) throws Exception {
        if (isHave(request.getUsername())) {
            throw new Exception("Username oldindan mavjud");
        } else {
            // save image logic
            // teacher.setImageUrl(imgUrl);
            String imgUrl = "";
            try {
                if (request.isStudent()) studentRep.save(studentMap.toModel(request, imgUrl));
                else teacherRep.save(teacherMap.toModel(request, imgUrl));
                log.info("Saqlandi username: {}", request.getUsername());
            } catch (ConstraintViolationException e) {
                throw new Exception("Xatolik");
            }
        }
    }

    public boolean isHave(String username) {
        return teacherRep.existsTeacherByUsername(username) || studentRep.existsStudentByUsername(username);
    }
}
