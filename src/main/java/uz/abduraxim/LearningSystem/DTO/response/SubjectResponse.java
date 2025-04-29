package uz.abduraxim.LearningSystem.DTO.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectResponse {

    private UUID id;

    private String name;
}
