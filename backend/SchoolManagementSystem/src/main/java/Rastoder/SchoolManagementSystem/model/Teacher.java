package Rastoder.SchoolManagementSystem.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Teacher")
@NoArgsConstructor
@Getter
@Setter
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID teacherId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String familyName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private String description;

    @ElementCollection(targetClass = Language.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Teacher_Languages", joinColumns = @JoinColumn(name = "teacher_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Set<Language> languages;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<StudentGroup> studentGroups = new HashSet<>();

    @Builder
    public Teacher(String firstName, String familyName, String phoneNumber, String description, Set<Language> languages) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.languages = languages;
    }
}