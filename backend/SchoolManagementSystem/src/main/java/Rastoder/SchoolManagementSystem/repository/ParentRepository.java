package Rastoder.SchoolManagementSystem.repository;

import Rastoder.SchoolManagementSystem.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParentRepository extends JpaRepository<Parent, UUID> {
    List<Parent> findByIsActiveFalse();
    List<Parent> findByIsActiveTrue();
}
