package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.StudentGroupRequest;
import Rastoder.SchoolManagementSystem.dto.StudentGroupResponse;
import Rastoder.SchoolManagementSystem.dto.StudentRequest;
import Rastoder.SchoolManagementSystem.model.*;
import Rastoder.SchoolManagementSystem.repository.StudentGroupRepository;
import Rastoder.SchoolManagementSystem.repository.StudentRepository;
import Rastoder.SchoolManagementSystem.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class StudentGroupServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentGroupRepository studentGroupRepository;

    @InjectMocks
    private StudentGroupService underTest;

    @Captor
    private ArgumentCaptor<StudentGroup> groupCaptor;

    private UUID teacherId;
    private UUID studentId;
    private UUID groupId;
    private Teacher dummyTeacher;
    private Student dummyStudent;
    private StudentGroup dummyStudentGroup;

    @BeforeEach
    void setUp() {
        teacherId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        groupId = UUID.randomUUID();

        dummyTeacher = Teacher.builder()
                .firstName("Joe")
                .familyName("Swanson")
                .phoneNumber("111")
                .description("Best Teacher")
                .languages(Set.of(Language.BOSNIAN))
                .build();

        dummyStudent = Student.builder()
                .firstName("Max")
                .lastName("Maxen")
                .birthDate(LocalDate.of(2000, 12, 1))
                .level(1)
                .language(Language.BOSNIAN)
                .parents(new HashSet<>())
                .build();

        dummyStudentGroup = StudentGroup.builder()
                .groupId(groupId)
                .room(Room.ROOM_1)
                .students(new HashSet<>(Set.of(dummyStudent)))
                .teacher(dummyTeacher)
                .sessions(new HashSet<>())
                .build();

        dummyTeacher.setTeacherId(teacherId);
        dummyStudent.setStudentId(studentId);
    }

    @Test
    void createStudentGroup_shouldReturnCreatedStudentGroup() {
        // Arrange
        StudentGroupRequest request = new StudentGroupRequest(
                Room.ROOM_1,
                teacherId,
                Set.of(studentId)
        );

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(dummyTeacher));
        when(studentRepository.findAllById(Set.of(studentId))).thenReturn(List.of(dummyStudent));
        when(studentGroupRepository.save(any(StudentGroup.class))).thenReturn(dummyStudentGroup);

        StudentGroupResponse response = underTest.createStudentGroup(request);

        verify(teacherRepository).findById(teacherId);
        verify(studentRepository).findAllById(Set.of(studentId));
        verify(studentGroupRepository).save(groupCaptor.capture());
        verify(studentRepository).saveAll(Set.of(dummyStudent));

        StudentGroup capturedGroup = groupCaptor.getValue();
        assertThat(capturedGroup.getRoom()).isEqualTo(Room.ROOM_1);
        assertThat(capturedGroup.getTeacher()).isEqualTo(dummyTeacher);
        assertThat(capturedGroup.getStudents()).containsExactly(dummyStudent);

        assertThat(dummyStudent.getStudentGroup()).isEqualTo(dummyStudentGroup);

        assertThat(response).isNotNull();
        assertThat(response.groupId()).isEqualTo(dummyStudentGroup.getGroupId());
        assertThat(response.room()).isEqualTo(Room.ROOM_1);
        assertThat(response.teacherId()).isEqualTo(teacherId);
        assertThat(response.studentsIds()).containsExactly(studentId);
        assertThat(response.session()).isEmpty();
    }

    @Test
    void addStudentToStudentGroup_shouldAddStudentToGroup(){
        //Arrange
        dummyStudentGroup.setStudents(new HashSet<>());
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(dummyStudent));
    when(studentGroupRepository.findById(groupId)).thenReturn(Optional.of(dummyStudentGroup));
        //Act
        underTest.addStudentToStudentGroup(studentId,groupId);
        //Assert
        verify(studentRepository).findById(studentId);
        verify(studentGroupRepository).findById(groupId);
        verify(studentGroupRepository).save(groupCaptor.capture());
        StudentGroup capturedGroup = groupCaptor.getValue();
        assertThat(capturedGroup.getStudents()).containsExactly(dummyStudent);
    }


    @Test
    void getAllStudentGroups_shouldReturnAllStudentGroups() {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        Session dummySession = Session.builder().sessionId(sessionId).build();
        dummyStudentGroup.setSessions(Set.of(dummySession));

        StudentGroup emptyGroup = StudentGroup.builder()
                .groupId(UUID.randomUUID())
                .room(Room.ROOM_2)
                .teacher(null)
                .students(null)
                .sessions(null)
                .build();

        when(studentGroupRepository.findAll()).thenReturn(List.of(dummyStudentGroup, emptyGroup));

        // Act
        List<StudentGroupResponse> responses = underTest.getAllStudentGroups();

        // Assert
        verify(studentGroupRepository).findAll();
        assertThat(responses).hasSize(2);

        StudentGroupResponse first = responses.get(0);
        assertThat(first.groupId()).isEqualTo(groupId);
        assertThat(first.room()).isEqualTo(Room.ROOM_1);
        assertThat(first.teacherId()).isEqualTo(teacherId);
        assertThat(first.studentsIds()).containsExactly(studentId);
        assertThat(first.session()).containsExactly(sessionId);

        StudentGroupResponse second = responses.get(1);
        assertThat(second.groupId()).isEqualTo(emptyGroup.getGroupId());
        assertThat(second.teacherId()).isNull();
        assertThat(second.studentsIds()).isEmpty();
    }

    @Test
    void getStudentGroupById_shouldReturnStudentGroupById() {
        // Arrange
        when(studentGroupRepository.findById(groupId)).thenReturn(Optional.of(dummyStudentGroup));

        // Act
        StudentGroupResponse response = underTest.getStudentGroupById(groupId);

        // Assert
        verify(studentGroupRepository).findById(groupId);

        assertThat(response).isNotNull();
        assertThat(response.groupId()).isEqualTo(groupId);
        assertThat(response.room()).isEqualTo(Room.ROOM_1);
        assertThat(response.teacherId()).isEqualTo(teacherId);
        assertThat(response.studentsIds()).containsExactly(studentId);
    }

    @Test
    void assignTeacher_shouldReturnGroupWithAssignedTeacher() {
        // Arrange
        dummyStudentGroup.setTeacher(null);

        when(studentGroupRepository.findById(groupId)).thenReturn(Optional.of(dummyStudentGroup));
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(dummyTeacher));
        when(studentGroupRepository.save(any(StudentGroup.class))).thenReturn(dummyStudentGroup);

        // Act
        StudentGroupResponse response = underTest.assignTeacher(groupId, teacherId);

        // Assert
        verify(studentGroupRepository).findById(groupId);
        verify(teacherRepository).findById(teacherId);
        verify(studentGroupRepository).save(groupCaptor.capture());

        StudentGroup capturedGroup = groupCaptor.getValue();
        assertThat(capturedGroup.getGroupId()).isEqualTo(groupId);
        assertThat(capturedGroup.getTeacher()).isEqualTo(dummyTeacher);
        assertThat(capturedGroup.getRoom()).isEqualTo(Room.ROOM_1);

        assertThat(response).isNotNull();
        assertThat(response.groupId()).isEqualTo(groupId);
        assertThat(response.teacherId()).isEqualTo(teacherId);
        assertThat(response.room()).isEqualTo(Room.ROOM_1);
        assertThat(response.studentsIds()).containsExactly(studentId);
        assertThat(response.session()).isEmpty();
    }

    @Test
    void getStudentGroupByTeacherId(){
        //Arrange
        when(studentGroupRepository.findByTeacher_TeacherId(teacherId)).thenReturn(List.of(dummyStudentGroup));
        //Act
        List<StudentGroupResponse> responses = underTest.getStudentGroupByTeacherId(teacherId);
        //Assert
        verify(studentGroupRepository).findByTeacher_TeacherId(teacherId);

        assertThat(responses).hasSize(1);
        StudentGroupResponse first = responses.get(0);
        assertThat(first.groupId()).isEqualTo(groupId);
        assertThat(first.room()).isEqualTo(Room.ROOM_1);
        assertThat(first.teacherId()).isEqualTo(teacherId);
        assertThat(first.studentsIds()).containsExactly(studentId);
        assertThat(first.session()).isEmpty();
    }
}






















