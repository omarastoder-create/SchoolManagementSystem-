package Rastoder.SchoolManagementSystem.service;


import Rastoder.SchoolManagementSystem.dto.ParentResponse;
import Rastoder.SchoolManagementSystem.dto.StudentRequest;
import Rastoder.SchoolManagementSystem.dto.StudentResponse;
import Rastoder.SchoolManagementSystem.model.*;
import Rastoder.SchoolManagementSystem.repository.ParentRepository;
import Rastoder.SchoolManagementSystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceController {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ParentRepository parentRepository;

    @InjectMocks
    private StudentService underTest;

    @Captor
    private ArgumentCaptor<Student> studentCaptor;

    private Student dummyStudent;
    private Parent dummyParent;
    @BeforeEach
    void setUp(){
        dummyParent = new Parent(
                "Joe",
                "Swanson",
                "swanson@lol.com",
                "333333333"
        );
        dummyParent.setParentId(UUID.randomUUID());

        dummyStudent = Student.builder()
                .firstName("Max")
                .lastName("Hansen")
                .birthDate(LocalDate.of(2015, 5, 10))
                .level(1)
                .language(Language.LUXEMBOURGISH)
                .parents(Set.of(dummyParent))
                .build();
        dummyStudent.setStudentId(UUID.randomUUID());

    }

    @Test
    void createStudent_shouldReturnCreatedStudent(){
        //Arrange
        StudentRequest request = new StudentRequest(
                "Max",
                "Hansen",
                LocalDate.of(2015, 5, 10),
                1,
                Language.LUXEMBOURGISH,
                Set.of(dummyParent.getParentId())
        );


        when(parentRepository.findAllById(Set.of(dummyParent.getParentId())))
                .thenReturn(List.of(dummyParent));
        when(studentRepository.save(any(Student.class))).thenReturn(dummyStudent);

        //Act
        StudentResponse response = underTest.createStudent(request);

        //Assert
        verify(parentRepository).findAllById(Set.of(dummyParent.getParentId()));
        verify(studentRepository).save(studentCaptor.capture());
        Student capturedStudent = studentCaptor.getValue();

        assertThat(response.firstName()).isEqualTo("Max");

        assertThat(capturedStudent.getStudentId()).isNull();
        assertThat(capturedStudent.getFirstName()).isEqualTo("Max");
        assertThat(capturedStudent.getLastName()).isEqualTo("Hansen");
        assertThat(capturedStudent.getBirthDate()).isEqualTo(LocalDate.of(2015, 5, 10));
        assertThat(capturedStudent.getLanguage()).isEqualTo(Language.LUXEMBOURGISH);
        assertThat(capturedStudent.getParents()).containsExactly(dummyParent);
        assertThat(capturedStudent.isActive()).isTrue();

        assertThat(response.studentId()).isEqualTo(dummyStudent.getStudentId());
        assertThat(response.firstName()).isEqualTo("Max");
        assertThat(response.lastName()).isEqualTo("Hansen");
        assertThat(response.birthDate()).isEqualTo(LocalDate.of(2015, 5, 10));
        assertThat(response.language()).isEqualTo(Language.LUXEMBOURGISH);
        assertThat(response.isActive()).isTrue();

    }

    @Test
    void getAllStudents_shouldReturnAllActiveStudents(){
        //Arrange
        when(studentRepository.findByIsActiveTrue()).thenReturn(List.of(dummyStudent));
        //Act
        List<StudentResponse> students = underTest.getAllStudents();
        //assert
        verify(studentRepository).findByIsActiveTrue();

        assertThat(students).hasSize(1);
        assertThat(students.get(0).firstName()).isEqualTo("Max");
        assertThat(students.get(0).isActive()).isTrue();
        assertThat(students.get(0).studentId()).isEqualTo(dummyStudent.getStudentId());
    }

    @Test
    void getStudentById_shouldReturnStudentById(){
        //Arrange
        UUID studentId = dummyStudent.getStudentId();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(dummyStudent));
        //Act
        StudentResponse response = underTest.getStudentById(studentId);
        // Assert
        verify(studentRepository).findById(studentId);

        assertThat(response.studentId()).isEqualTo(dummyStudent.getStudentId());
        assertThat(response.firstName()).isEqualTo("Max");
        assertThat(response.isActive()).isTrue();
    }

    @Test
    void addStudentLevel_shouldAddStudentLevel(){
        //Arrange
        UUID studentId = dummyStudent.getStudentId();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(dummyStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(dummyStudent);

        //Act
        StudentResponse response =underTest.addStudentLevel(studentId);
        //Assert
        verify(studentRepository).save(studentCaptor.capture());
        verify(studentRepository).findById(studentId);
        Student capturedStudent = studentCaptor.getValue();
        assertThat(response.level()).isEqualTo(2);
        assertThat(capturedStudent.getLevel()).isEqualTo(2);
    }

    @Test
    void archiveStudentWithId_shouldArchiveStudentWithId(){
        //Arange
        UUID studentId = dummyStudent.getStudentId();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(dummyStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(dummyStudent);
        //Act
        StudentResponse response = underTest.archiveStudentWithId(studentId);

        //Assert
        verify(studentRepository).save(studentCaptor.capture());
        verify(studentRepository).findById(studentId);
        Student capturedStudent = studentCaptor.getValue();
        verify(studentRepository,never()).delete(any());//Important

        assertThat(response.isActive()).isFalse();
        assertThat(response.studentId()).isEqualTo(dummyStudent.getStudentId());

        assertThat(capturedStudent.isActive()).isFalse();
        assertThat(capturedStudent.getStudentId()).isEqualTo(dummyStudent.getStudentId());
    }

    @Test
    void getAllNonActiveStudents_shouldReturnAllNonActiveStudents(){
        //Arrange
        dummyStudent.setActive(false);
        when(studentRepository.findByIsActiveFalse()).thenReturn(List.of(dummyStudent));
        //Act
        List<StudentResponse> students = underTest.getAllNonActiveStudents();
        //Assert
        verify(studentRepository).findByIsActiveFalse();
        assertThat(students).hasSize(1);
        assertThat(students.get(0).isActive()).isFalse();
        assertThat(students.get(0).studentId()).isEqualTo(dummyStudent.getStudentId());
    }

    @Test
    void getAllStudentsWithGroupId_shouldReturnAllStudentsWithGroupId() {
        // Arrange
        UUID groupId = UUID.randomUUID();
        when(studentRepository.findActiveStudentByGroupId(groupId)).thenReturn(List.of(dummyStudent));

        // Act
        List<StudentResponse> students = underTest.getAllStudentsWithGroupdId(groupId);

        // Assert
        verify(studentRepository).findActiveStudentByGroupId(groupId);

        assertThat(students).hasSize(1);
        assertThat(students.get(0).studentId()).isEqualTo(dummyStudent.getStudentId());
        assertThat(students.get(0).firstName()).isEqualTo("Max");
        assertThat(students.get(0).isActive()).isTrue();

        //TODO add in the response the id which is missign for the studentgroup and rewrite the code
    }
    @Test
    void getAllStudentsWithLanguage(){
        //Arrange
        Language language = Language.LUXEMBOURGISH;
        dummyStudent.setLanguage(language);
        when(studentRepository.findByLanguageAndIsActiveTrue(language)).thenReturn(List.of(dummyStudent));

        // Act
        List<StudentResponse> students = underTest.getAllStudentsWithLanguage(language);
        //Assert
        verify(studentRepository).findByLanguageAndIsActiveTrue(language);

        assertThat(students).hasSize(1);
        assertThat(students.get(0).language()).isEqualTo(language);
        assertThat(students.get(0).studentId()).isEqualTo(dummyStudent.getStudentId());
        assertThat(students.get(0).firstName()).isEqualTo("Max");

    }
}
