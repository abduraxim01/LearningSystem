package uz.abduraxim.LearningSystem.DTO.request;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForChangeDetails {

    private String id;

    private String newName;

    private String newUsername;

    private String newPassword;

    private String imgUrl;

    private Boolean isStudent;

    private List<String> subjectIds;
}
