package Rastoder.SchoolManagementSystem.repository;

import Rastoder.SchoolManagementSystem.model.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroup, UUID> {
}
