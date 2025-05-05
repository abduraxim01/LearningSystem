package uz.abduraxim.LearningSystem.service.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.abduraxim.LearningSystem.DTO.ResponseStructure;
import uz.abduraxim.LearningSystem.DTO.response.AuthResponse;
import uz.abduraxim.LearningSystem.mapper.StudentMapper;
import uz.abduraxim.LearningSystem.mapper.TeacherMapper;
import uz.abduraxim.LearningSystem.model.Student;
import uz.abduraxim.LearningSystem.model.Teacher;
import uz.abduraxim.LearningSystem.repository.StudentRepository;
import uz.abduraxim.LearningSystem.repository.TeacherRepository;
import uz.abduraxim.LearningSystem.service.jwtService.JwtUtil;

import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    private final StudentRepository studentRep;

    private final TeacherRepository teacherRep;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final JwtUtil jwtUtil;

    private final StudentMapper studentMap;

    private final TeacherMapper teacherMap;

    private final ResponseStructure response = new ResponseStructure();

    private final Logger log = LogManager.getLogger(AuthService.class);

    @Autowired
    public AuthService(StudentRepository studentRep, TeacherRepository teacherRep, JwtUtil jwtUtil, StudentMapper studentMap, TeacherMapper teacherMap) {
        this.studentRep = studentRep;
        this.teacherRep = teacherRep;
        this.jwtUtil = jwtUtil;
        this.studentMap = studentMap;
        this.teacherMap = teacherMap;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (studentRep.existsStudentByUsername(username)) {
            return studentRep.findStudentByUsername(username);
        } else if (teacherRep.existsTeacherByUsername(username)) {
            return teacherRep.findTeacherByUsername(username);
        }
        return null;
    }

    public ResponseStructure login(String username, String password) {
        ResponseStructure response = new ResponseStructure();
        UserDetails user = this.loadUserByUsername(username);
        if (user != null && encoder.matches(password, user.getPassword())) {
            response.setSuccess(true);
            response.setMessage("Muvafaqqiyatli");
            response.setData(AuthResponse.builder()
                    .username(username)
                    .role(user.getAuthorities().toString().substring(6, user.getAuthorities().toString().length() - 1))
                    .token(jwtUtil.encode(username, user.getAuthorities()))
                    .build());
            log.info("Login muvafaqqiyatli, Username: {}", username);
        } else {
            response.setSuccess(false);
            response.setMessage("Password yoki username xato");
            log.info("Login muvafaqqiyatsiz, Username: {}", username);
        }
        return response;
    }

    public ResponseStructure getCurrentUser(Authentication authentication, String username) {
        UUID userId;
        if (studentRep.existsStudentByUsername(username)) {
            userId = ((Student) authentication.getPrincipal()).getId();
            Student student = studentRep.findStudentByUsername(username);
            if (student.getId().equals(userId)) {
                response.setSuccess(true);
                response.setMessage("");
                response.setData(studentMap.toDTO(studentRep.findStudentByUsername(username)));
            } else {
                response.setSuccess(false);
                response.setMessage("Noto'g'ri so'rov");
                response.setData(null);
            }
        } else if (teacherRep.existsTeacherByUsername(username)) {
            userId = ((Teacher) authentication.getPrincipal()).getId();
            Teacher teacher = teacherRep.findTeacherByUsername(username);
            if (teacher.getId().equals(userId)) {
                response.setSuccess(true);
                response.setMessage("");
                response.setData(teacherMap.toDTO(teacherRep.findTeacherByUsername(username)));
            } else {
                response.setSuccess(false);
                response.setMessage("Noto'g'ri so'rov");
                response.setData(null);
            }
        } else {
            response.setSuccess(false);
            response.setMessage("User topilmadi");
            response.setData(null);
        }
        return response;
    }
}