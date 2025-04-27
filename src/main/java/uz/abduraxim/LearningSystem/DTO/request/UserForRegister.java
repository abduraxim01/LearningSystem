package uz.abduraxim.LearningSystem.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForRegister {

    private String name;

    private String username;

    private String password;

    private boolean isStudent;
}
