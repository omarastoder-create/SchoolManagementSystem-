package Rastoder.SchoolManagementSystem.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Student")
@Getter @Setter
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true,updatable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthDate;


    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // important
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",nullable = false)
    private Parent parent;

    @Builder
    public Student(String firstName, String lastName, LocalDate birthDate, int level, Language language, Parent parent) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.level = level;
        this.language = language;
        this.parent = parent;
    }
}
