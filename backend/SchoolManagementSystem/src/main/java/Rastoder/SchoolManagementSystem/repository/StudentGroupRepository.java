package Rastoder.SchoolManagementSystem.repository;

import Rastoder.SchoolManagementSystem.model.Student;
import Rastoder.SchoolManagementSystem.model.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroup, UUID> {

    // 1. The @Query annotation tells Spring we are writing a custom database command.
    // 2. "SELECT s" means give me the Student objects.
    // 3. "FROM StudentGroup sg JOIN sg.students s" links the Group class to the Student class.
    // 4. "WHERE sg.groupId = :id" filters the results by the ID we pass in.
    @Query("SELECT s FROM StudentGroup sg JOIN sg.students s " +
            "WHERE sg.groupId = :id AND s.isActive = true")
    List<Student> findActiveStudentsByGroupId(@Param("id") UUID id);
}
