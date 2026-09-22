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
    List<StudentGroup> findByTeacher_TeacherId(UUID teacherId);
}
