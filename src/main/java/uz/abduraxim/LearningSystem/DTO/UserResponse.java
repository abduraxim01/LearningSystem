package uz.abduraxim.LearningSystem.DTO;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private UUID id;

    private String name;

    private String username;

    private String imageUrl;
}
