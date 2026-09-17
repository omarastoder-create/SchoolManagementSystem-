package Rastoder.SchoolManagementSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="StudentGroup")
public class StudentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false)
    private UUID groupId;

    @Enumerated(EnumType.STRING)
    private Room room;

    @OneToMany(mappedBy = "studentGroup", cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    // NEW: Each group has multiple sessions
    @OneToMany(mappedBy = "studentGroup", cascade = CascadeType.ALL)
    private Set<Session> sessions;
}