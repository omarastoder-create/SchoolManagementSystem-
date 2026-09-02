package Rastoder.SchoolManagementSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="StudentGroup")
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "studentId")
    private UUID groupId;

    @Enumerated(EnumType.STRING)
    private Room room;
}
