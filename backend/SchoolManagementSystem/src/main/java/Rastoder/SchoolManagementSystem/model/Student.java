package Rastoder.SchoolManagementSystem.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Student")
@Getter @Setter
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false)
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
    @Enumerated(EnumType.STRING)
    private Language language;

    // FIX: Changed from @ManyToOne to @ManyToMany for multiple parents
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "student_parent",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id")
    )
    private Set<Parent> parents = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private StudentGroup studentGroup;

    @Builder
    public Student(String firstName, String lastName, LocalDate birthDate, int level, Language language, Set<Parent> parents, StudentGroup studentGroup) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.level = level;
        this.language = language;
        this.parents = parents != null ? parents : new HashSet<>();
        this.studentGroup = studentGroup;
    }
}