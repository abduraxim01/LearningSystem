package uz.abduraxim.LearningSystem.DTO;

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
