package uz.abduraxim.LearningSystem.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.abduraxim.LearningSystem.DTO.ResponseStructure;
import uz.abduraxim.LearningSystem.DTO.UserResponse;
import uz.abduraxim.LearningSystem.DTO.response.AuthResponse;
import uz.abduraxim.LearningSystem.mapper.StudentMapper;
import uz.abduraxim.LearningSystem.mapper.TeacherMapper;
import uz.abduraxim.LearningSystem.repository.StudentRepository;
import uz.abduraxim.LearningSystem.repository.TeacherRepository;
import uz.abduraxim.LearningSystem.service.jwtService.JwtUtil;

@Service
public class AuthService implements UserDetailsService {

    private final StudentRepository studentRep;

    private final TeacherRepository teacherRep;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final JwtUtil jwtUtil;

    private final StudentMapper studentMap;

    private final TeacherMapper teacherMap;

    private final ResponseStructure response = new ResponseStructure();

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
        if (user == null) {
            response.setSuccess(false);
            response.setMessage("Password yoki username xato");
            return response;
        }
        if (encoder.matches(password, user.getPassword())) {
            response.setSuccess(true);
            response.setMessage("Muvafaqqiyatli");
            response.setData(AuthResponse.builder()
                    .username(username)
                    .role(user.getAuthorities().toString().substring(6, user.getAuthorities().toString().length() - 1))
                    .token(jwtUtil.encode(username, user.getAuthorities()))
                    .build());
        }
        return response;
    }

    public ResponseStructure getCurrentUser(String username) {
        if (studentRep.existsStudentByUsername(username)) {
            response.setSuccess(true);
            response.setData(studentMap.toDTO(studentRep.findStudentByUsername(username)));
        } else if (teacherRep.existsTeacherByUsername(username)) {
            response.setSuccess(true);
            response.setData(teacherMap.toDTO(teacherRep.findTeacherByUsername(username)));
        } else {
            response.setSuccess(false);
            response.setMessage("User topilmadi");
        }
        return response;
    }
}