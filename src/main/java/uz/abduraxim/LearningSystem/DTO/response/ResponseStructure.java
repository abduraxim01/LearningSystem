package uz.abduraxim.LearningSystem.DTO.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseStructure {

    private boolean success;

    private String message;

    private Object data;
}
