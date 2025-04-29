package uz.abduraxim.LearningSystem.DTO.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForChangeDetails {

    private String newName;

    private String newUsername;
}
