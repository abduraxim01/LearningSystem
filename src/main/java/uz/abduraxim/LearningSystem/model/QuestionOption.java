package uz.abduraxim.LearningSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOption {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isCorrect;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "question_id")
    private Question question;
}
