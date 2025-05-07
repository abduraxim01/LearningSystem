package uz.abduraxim.LearningSystem.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.abduraxim.LearningSystem.DTO.request.AnswersRequest;
import uz.abduraxim.LearningSystem.DTO.request.AttachSubject;
import uz.abduraxim.LearningSystem.DTO.request.UserForChangeDetails;
import uz.abduraxim.LearningSystem.DTO.request.UserForRegister;
import uz.abduraxim.LearningSystem.DTO.response.ResponseStructure;
import uz.abduraxim.LearningSystem.mapper.StudentMapper;
import uz.abduraxim.LearningSystem.mapper.SubjectMapper;
import uz.abduraxim.LearningSystem.mapper.TeacherMapper;
import uz.abduraxim.LearningSystem.model.Answer;
import uz.abduraxim.LearningSystem.model.Student;
import uz.abduraxim.LearningSystem.model.Subject;
import uz.abduraxim.LearningSystem.model.Teacher;
import uz.abduraxim.LearningSystem.repository.AnswerRepository;
import uz.abduraxim.LearningSystem.repository.StudentRepository;
import uz.abduraxim.LearningSystem.repository.SubjectRepository;
import uz.abduraxim.LearningSystem.repository.TeacherRepository;

import java.util.*;
import java.util.stream.Collectors;

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

    private final AnswerRepository answerRep;

    @Autowired
    public AdminService(TeacherRepository teacherRep, StudentRepository studentRep, TeacherMapper teacherMap, StudentMapper studentMap, SubjectRepository subjectRep, SubjectMapper subjectMap, AnswerRepository answerRep) {
        this.teacherRep = teacherRep;
        this.studentRep = studentRep;
        this.teacherMap = teacherMap;
        this.studentMap = studentMap;
        this.subjectRep = subjectRep;
        this.subjectMap = subjectMap;
        this.answerRep = answerRep;
    }

    public ResponseStructure getCorrectAnswerCounts(AnswersRequest request) {
        UUID studentId = null;
        UUID subjectId = null;

        try {
            if (request.getStudentId() != null && !request.getStudentId().isBlank()) {
                studentId = UUID.fromString(request.getStudentId());
                System.out.println("StudentId: " + studentId);
            }
            if (request.getSubjectId() != null && !request.getSubjectId().isBlank()) {
                subjectId = UUID.fromString(request.getSubjectId());
                System.out.println("SubjectId: " + subjectId);
            }
        } catch (Exception e) {
            response.setMessage("ID noto‘g‘ri formatda");
            response.setSuccess(false);
            return response;
        }

        List<Answer> answers = answerRep.findAll(); // barcha javoblar

        // Faqat to‘g‘ri javoblar
        UUID finalStudentId = studentId;
        UUID finalStudentId1 = studentId;
        UUID finalSubjectId = subjectId;
        List<Answer> correctAnswers = answers.stream()
                .filter(Answer::isCorrect)
                .filter(ans -> finalStudentId == null || ans.getStudent().getId().equals(finalStudentId1))
                .filter(ans -> finalSubjectId == null || ans.getQuestion().getSubject().getId().equals(finalSubjectId))
                .toList();

        // Group by Student ID + Subject Name
        Map<String, Map<String, Long>> grouped = correctAnswers.stream()
                .collect(Collectors.groupingBy(
                        ans -> ans.getStudent().getUsername(),
                        Collectors.groupingBy(
                                ans -> ans.getQuestion().getSubject().getName(),
                                Collectors.counting()
                        )
                ));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, Long>> studentEntry : grouped.entrySet()) {
            String studentUsername = studentEntry.getKey();
            for (Map.Entry<String, Long> subjectEntry : studentEntry.getValue().entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("studentName", studentRep.findStudentByUsername(studentUsername).getName());
                item.put("username", studentUsername);
                item.put("subject", subjectEntry.getKey());
                item.put("count", subjectEntry.getValue());
                result.add(item);
            }
        }

        response.setData(result);
        response.setSuccess(true);
        response.setMessage("");
        return response;
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

    public ResponseStructure updateUserDetails(UserForChangeDetails user) {
        if (isHave(user.getNewUsername())) {
            if (user.getIsStudent()) {
                if (!UUID.fromString(user.getId()).equals(studentRep.findStudentByUsername(user.getNewUsername()).getId())) {
                    response.setSuccess(false);
                    response.setMessage("Username mavjud");
                    response.setData(null);
                    return response;
                }
            } else if (!UUID.fromString(user.getId()).equals(teacherRep.findTeacherByUsername(user.getNewUsername()).getId())) {
                response.setSuccess(false);
                response.setMessage("Username mavjud");
                response.setData(null);
                return response;
            }
        }
        try {
            if (user.getIsStudent()) {
                Student student = studentRep.findById(UUID.fromString(user.getId())).get();
                studentRep.save(studentMap.toModel(student, user));
                return attachSubject(AttachSubject.builder()
                        .studentId(user.getId())
                        .subjectIds(user.getSubjectIds())
                        .build());
            } else {
                Teacher teacher = teacherRep.findById(UUID.fromString(user.getId())).get();
                Subject subject = subjectRep.findById(UUID.fromString(user.getSubjectIds().get(0))).get();
                teacher.setSubject(subject);
                teacherRep.save(teacherMap.toModel(teacher, user.getNewName(), user.getNewUsername(), user.getNewPassword(), user.getImgUrl()));
            }
            response.setSuccess(true);
            response.setMessage("");
            response.setData(null);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("User topilmadi yoki ko'zda tutilmagan xatolik");
            response.setData(null);
        }

        return response;
    }

    public ResponseStructure attachSubject(AttachSubject attachSubject) {
        response.setMessage("");
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

    public ResponseStructure getSubjectList(String username) {
        try {
            if (studentRep.existsStudentByUsername(username)) {
                response.setData(subjectMap.toDTO(studentRep.findStudentByUsername(username).getSubject()));
                response.setSuccess(true);
                response.setMessage("");
            } else if (teacherRep.existsTeacherByUsername(username) && username.equals("admin")) {
                response.setData(subjectMap.toDTO(subjectRep.findAll()));
                response.setSuccess(true);
                response.setMessage("");
            } else if (teacherRep.existsTeacherByUsername(username)) {
                response.setData(subjectMap.toDTO(teacherRep.findTeacherByUsername(username).getSubject()));
                response.setSuccess(true);
                response.setMessage("");
            } else {
                response.setSuccess(false);
                response.setMessage("Foydalanuvchi topilmadi");
                response.setData(null);
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Noto'gri so'rov");
            response.setData(null);
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
