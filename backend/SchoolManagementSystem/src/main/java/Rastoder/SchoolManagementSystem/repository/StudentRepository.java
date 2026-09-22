package Rastoder.SchoolManagementSystem.repository;

import Rastoder.SchoolManagementSystem.model.Language;
import Rastoder.SchoolManagementSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    List<Student> findByIsActiveTrue();
    List<Student> findByIsActiveFalse();
    List<Student> findByLanguageAndIsActiveTrue(Language language);

    @Query("SELECT s FROM StudentGroup sg JOIN sg.students s WHERE sg.groupId = :id AND s.isActive = true")
    List<Student> findActiveStudentByGroupId(@Param("id") UUID id);
}
