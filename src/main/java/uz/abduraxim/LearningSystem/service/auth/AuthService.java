package uz.abduraxim.LearningSystem.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.abduraxim.LearningSystem.DTO.response.AuthResponse;
import uz.abduraxim.LearningSystem.repository.StudentRepository;
import uz.abduraxim.LearningSystem.repository.TeacherRepository;
import uz.abduraxim.LearningSystem.service.jwtService.JwtUtil;

@Service
public class AuthService implements UserDetailsService {

    private final StudentRepository studentRep;

    private final TeacherRepository teacherRep;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(StudentRepository studentRep, TeacherRepository teacherRep, JwtUtil jwtUtil) {
        this.studentRep = studentRep;
        this.teacherRep = teacherRep;
        this.jwtUtil = jwtUtil;
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

    public AuthResponse login(String username, String password) throws Exception {
        UserDetails user = this.loadUserByUsername(username);
        if (encoder.matches(password, user.getPassword())) {
            return AuthResponse.builder()
                    .username(username)
                    .role(user.getAuthorities().toString().substring(6, user.getAuthorities().toString().length() - 1))
                    .token(jwtUtil.encode(username, user.getAuthorities()))
                    .build();
        } else {
            throw new Exception("Password xato");
        }
    }
}
