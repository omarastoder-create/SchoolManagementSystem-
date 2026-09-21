package Rastoder.SchoolManagementSystem.repository;

import Rastoder.SchoolManagementSystem.model.Student;
import Rastoder.SchoolManagementSystem.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
    List<Teacher> findByIsActiveTrue();
    List<Teacher> findByIsActiveFalse();
}
