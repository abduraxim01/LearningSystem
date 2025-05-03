package uz.abduraxim.LearningSystem.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import uz.abduraxim.LearningSystem.DTO.ResponseStructure;
import uz.abduraxim.LearningSystem.DTO.request.AttachSubject;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    private final TeacherRepository teacherRep;

    private final StudentRepository studentRep;

    private final Logger log = LogManager.getLogger(AdminService.class);

    private final TeacherMapper teacherMap;

    private final StudentMapper studentMap;

    private final SubjectRepository subjectRep;

    private final SubjectMapper subjectMap;

    private final ResponseStructure response = new ResponseStructure();

    @Autowired
    public AdminService(TeacherRepository teacherRep, StudentRepository studentRep, TeacherMapper teacherMap, StudentMapper studentMap, SubjectRepository subjectRep, SubjectMapper subjectMap) {
        this.teacherRep = teacherRep;
        this.studentRep = studentRep;
        this.teacherMap = teacherMap;
        this.studentMap = studentMap;
        this.subjectRep = subjectRep;
        this.subjectMap = subjectMap;
    }

    @Transactional
    public ResponseStructure deleteUser(String username) {
        boolean studentExists = studentRep.existsStudentByUsername(username);
        boolean teacherExists = teacherRep.existsTeacherByUsername(username);
        if (studentExists || teacherExists) {
            if (studentExists) studentRep.deleteStudentByUsername(username);
            if (teacherExists) teacherRep.deleteTeacherByUsername(username);
            response.setSuccess(true);
            response.setMessage("O'chirildi");
        } else {
            response.setSuccess(false);
            response.setMessage("Foydalanuvchi topilmadi");
        }
        response.setData(null); // Optional: add info about the deleted user
        return response;
    }

    public ResponseStructure updateUserDetails(String userId, String newName, String newUsername, String newPassword, String imgUrl, boolean isStudent) {
        if (isHave(newUsername)) {
            response.setSuccess(false);
            response.setMessage("Username mavjud");
            response.setData(null);
        } else {
            try {
                if (isStudent) {
                    Student student = studentRep.findById(UUID.fromString(userId)).get();
                    studentRep.save(studentMap.toModel(student, newName, newUsername, newPassword, imgUrl));
                } else {
                    Teacher teacher = teacherRep.findById(UUID.fromString(userId)).get();
                    teacherRep.save(teacherMap.toModel(teacher, newName, newUsername, newPassword, imgUrl));
                }
                response.setSuccess(true);
                response.setMessage("");
                response.setData(null);
            } catch (Exception e) {
                response.setSuccess(false);
                response.setMessage("User topilmadi yoki ko'zda tutilmagan xatolik");
                response.setData(null);
            }
        }
        return response;
    }

    public ResponseStructure attachSubject(AttachSubject attachSubject) {
        try {
            Student student = studentRep.findById(UUID.fromString(attachSubject.getStudentId())).get();
            List<Subject> studentSubjectList = student.getSubject();
            List<Subject> subjectList = attachSubject.getSubjectIds().stream()
                    .map(id -> subjectRep.findById(UUID.fromString(id)).get())
                    .toList();
            List<String> alreadyExistsSubjectId = new ArrayList<>();
            for (Subject subject : subjectList) {
                if (studentSubjectList.contains(subject)) {
                    response.setMessage("Avvaldan biriktirilgan fanlar mavjud");
                    alreadyExistsSubjectId.add(subject.getId().toString());
                } else {
                    studentSubjectList.add(subject);
                }
            }
            student.setSubject(studentSubjectList);
            studentRep.save(student);
            response.setSuccess(true);
            response.setData(alreadyExistsSubjectId);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Student yoki Fan topilmadi");
            response.setData(null);
        }
        return response;
    }

    public ResponseStructure getSubjectList(Authentication authentication, String isStudent) {
        ResponseStructure response = new ResponseStructure();
        try {
            if (isStudent.equals("true")) {
                UUID userId = ((Student) authentication.getPrincipal()).getId();
                Student student = studentRep.findById(userId).get();
                response.setSuccess(true);
                response.setMessage("");
                response.setData(subjectMap.toDTO(student.getSubject()));
            } else if (isStudent.equals("false")) {
                response.setSuccess(true);
                response.setMessage("");
                response.setData(subjectMap.toDTO(subjectRep.findAll()));
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Noto'gri so'rov");
        }
        return response;
    }

    public ResponseStructure getAllTeachers() {
        response.setSuccess(true);
        response.setMessage("");
        response.setData(teacherMap.toDTO(teacherRep.findAll()));
        return response;
    }

    public ResponseStructure getAllStudents() {
        response.setSuccess(true);
        response.setMessage("");
        response.setData(studentMap.toDTO(studentRep.findAll()));
        return response;
    }

    public ResponseStructure updateSubject(String subjectId, String newName) {
        if (subjectRep.existsSubjectByName(newName)) {
            response.setSuccess(false);
            response.setMessage("Fan nomi oldindan mavjud");
        } else {
            try {
                Subject subject = subjectRep.findById(UUID.fromString(subjectId)).get();
                subject.setName(newName);
                subjectRep.save(subject);
                response.setSuccess(true);
                response.setMessage("");
            } catch (Exception e) {
                response.setSuccess(false);
                response.setMessage("Fan topilmadi");
            }
        }
        return response;
    }

    public ResponseStructure deleteSubject(String subjectId) {
        try {
            UUID id = UUID.fromString(subjectId);
            subjectRep.deleteById(id);
            response.setSuccess(true);
            response.setData(null);
            response.setMessage(null);
        } catch (Exception e) {
            response.setMessage("Topilmadi");
            response.setSuccess(true);
            response.setData(null);
        }
        return response;
    }

    public ResponseStructure addSubject(String name) {
        if (!subjectRep.existsSubjectByName(name)) {
            response.setSuccess(true);
            response.setMessage("");
            response.setData(subjectRep.save(Subject.builder()
                    .name(name)
                    .build()).getId());
        } else {
            response.setSuccess(false);
            response.setMessage("Fan oldindan mavjud");
            response.setData(null);
        }
        return response;
    }

    public ResponseStructure addUser(UserForRegister request, String subjectId) {
        Subject subject;
        try {
            subject = subjectRep.findById(UUID.fromString(subjectId)).orElseThrow();
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Fan topilmadi");
            response.setData(null);
            return response;
        }
        if (isHave(request.getUsername())) {
            response.setSuccess(false);
            response.setMessage("Username oldindan mavjud");
            response.setData(null);
            return response;
        } else {
            try {
                if (request.getIsStudent()) studentRep.save(studentMap.toModel(request, subject));
                else teacherRep.save(teacherMap.toModel(request, subject));
                log.info("Saqlandi username: {}", request.getUsername());
                response.setSuccess(true);
                response.setMessage("Saqlandi");
                response.setData(null);
                return response;
            } catch (ConstraintViolationException e) {
                response.setSuccess(false);
                response.setMessage("Ko'zda tutilmagan xatolik");
                response.setData(null);
                return response;
            }
        }
    }

    public boolean isHave(String username) {
        return teacherRep.existsTeacherByUsername(username) || studentRep.existsStudentByUsername(username);
    }
}
