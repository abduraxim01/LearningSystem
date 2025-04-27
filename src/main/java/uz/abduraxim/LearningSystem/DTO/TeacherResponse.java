package uz.abduraxim.LearningSystem.DTO;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Valid
public class TeacherResponse {

    private UUID id;

    private String name;

    private String username;

    private String password;

    private String imageUrl;
}
