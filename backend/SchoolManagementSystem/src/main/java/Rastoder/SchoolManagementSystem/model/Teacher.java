package Rastoder.SchoolManagementSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Table
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true,  nullable = false , updatable = false)
    private UUID teacherId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String familyName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;


    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Language> languages;


}
