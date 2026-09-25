package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.TeacherRequest;
import Rastoder.SchoolManagementSystem.dto.TeacherResponse;
import Rastoder.SchoolManagementSystem.model.Language;
import Rastoder.SchoolManagementSystem.model.Teacher;
import Rastoder.SchoolManagementSystem.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository ;

    @InjectMocks
    private TeacherService underTest;

    @Captor
    private ArgumentCaptor<Teacher> teacherCaptor;

    private Teacher dummyTeacher;

    @BeforeEach
    void setUp(){
        dummyTeacher = Teacher.builder()
                .firstName("Kenan")
                .familyName("Korac")
                .phoneNumber("691691925")
                .description("Cool dude")
                .languages(Set.of(Language.BOSNIAN))
                .build();

        dummyTeacher.setTeacherId(UUID.randomUUID());
    }

    @Test
    void createTeacher_ShouldMapAndSaveTeacher(){
        //Arrange
        TeacherRequest request = new TeacherRequest(
                "Kenan",
                "Korac",
                "691691925",
                "Cool dude",
                Set.of(Language.BOSNIAN)
        );


        when(teacherRepository.save(any(Teacher.class))).thenReturn(dummyTeacher);

        //Act
        TeacherResponse response = underTest.createTeacher(request);

        //Assert
        verify(teacherRepository).save(teacherCaptor.capture());
        Teacher capturedTeacher = teacherCaptor.getValue();

        //Mapping-Valiedrung

        assertThat(capturedTeacher.getFirstName()).isEqualTo("Kenan");
        assertThat(capturedTeacher.getFamilyName()).isEqualTo("Korac");
        assertThat(capturedTeacher.getPhoneNumber()).isEqualTo("691691925");

        assertThat(response.firstName()).isEqualTo("Kenan");
        assertThat(response.familyName()).isEqualTo("Korac");
    }

    @Test
    void getAllActiveTeachers_shouldGiveAllTeachersBack(){
        //Arrange
        when(teacherRepository.findByIsActiveTrue()).thenReturn(List.of(dummyTeacher));

        //Act
        List<TeacherResponse> responses = underTest.getAllActiveTeachers();

        //Assert
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).familyName()).isEqualTo("Korac");

        verify(teacherRepository).findByIsActiveTrue();
    }


    @Test
    void getTeacherById_shouldReturnTeacherById(){
        //Arrange
        UUID target = dummyTeacher.getTeacherId();
        when(teacherRepository.findById(target)).thenReturn(Optional.of(dummyTeacher));
        //Act
        TeacherResponse response = underTest.getTeacherById(target);
        //Assert
        assertThat(response.teacherId()).isEqualTo(target);
        assertThat(response.firstName()).isEqualTo("Kenan");
        verify(teacherRepository).findById(target);
    }

    @Test
    void updateTeacherWithId_shouldReturnUpdatedTeacher(){
        //Arrange
        UUID target = dummyTeacher.getTeacherId();
        TeacherRequest updateRequest = new TeacherRequest(
                "Kenan",
                "Korac",
                "691691924",
                "Cool dude 2",
                Set.of(Language.LUXEMBOURGISH)
        );

        when(teacherRepository.findById(target)).thenReturn(Optional.of(dummyTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(dummyTeacher);

        //Act
        TeacherResponse response = underTest.updateTeacherWithId(target,updateRequest);
        // --- ASSERT DEEP DIVE ---

        // Phase 1: Interaktions-Kontrolle & Abfangen (Mockito)
        verify(teacherRepository).findById(target);
        verify(teacherRepository).save(teacherCaptor.capture());
        Teacher capturedTeacher = teacherCaptor.getValue();

        // Phase 2: Datenbank-Zustand prüfen (Was geht in die DB rein?)
        assertThat(capturedTeacher.getDescription()).isEqualTo("Cool dude 2");
        assertThat(capturedTeacher.getPhoneNumber()).isEqualTo("691691924");
        assertThat(capturedTeacher.getLanguages()).containsExactlyInAnyOrder(Language.LUXEMBOURGISH);

        // Schutz-Check (Datenintegrität): Wurden nicht-editierbare Felder geschützt?
        assertThat(capturedTeacher.getFirstName()).isEqualTo("Kenan");
        assertThat(capturedTeacher.getFamilyName()).isEqualTo("Korac");

        // Phase 3: Rückgabe-Zustand prüfen (Was geht an den Controller raus?)
        assertThat(response.description()).isEqualTo("Cool dude 2");
        assertThat(response.phoneNumber()).isEqualTo("691691924");
        assertThat(response.languages()).containsExactlyInAnyOrder(Language.LUXEMBOURGISH);
    }

    @Test
    void updateTeacherToInactive_shouldReturnInactiveTeacher(){
        //Arrange
        UUID target = dummyTeacher.getTeacherId();

        when(teacherRepository.findById(target)).thenReturn(Optional.of(dummyTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(dummyTeacher);

        //Act
        TeacherResponse response = underTest.updateTeacherToInactive(target);

        // Assert
        verify(teacherRepository).findById(target);
        verify(teacherRepository).save(teacherCaptor.capture());
        verify(teacherRepository, org.mockito.Mockito.never()).delete(any());
        Teacher capturedTeacher = teacherCaptor.getValue();

        assertThat(capturedTeacher.isActive()).isEqualTo(false); // db writing
        assertThat(response.isActive()).isFalse(); // response for the controller
    }

    @Test
    void getAllInactiveTeachers_shouldReturnAllInactiveTeachers(){
        //Arrange
        dummyTeacher.setActive(false);

        when(teacherRepository.findByIsActiveFalse()).thenReturn(List.of(dummyTeacher));

        //Act
        List<TeacherResponse> teachers = underTest.getAllInactiveTeachers();

        //Assert
        verify(teacherRepository).findByIsActiveFalse();

        assertThat(teachers).hasSize(1);
        assertThat(teachers.get(0).isActive()).isFalse();
        assertThat(teachers.get(0).firstName()).isEqualTo("Kenan");
        assertThat(teachers.get(0).teacherId()).isEqualTo(dummyTeacher.getTeacherId());
    }

    @Test
    void getTeacherById_shouldThrowException_whenIdDoesNotExist() {
        // Arrange
        UUID unknownId = UUID.randomUUID();
        when(teacherRepository.findById(unknownId)).thenReturn(Optional.empty());

        // Act & Assert
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> underTest.getTeacherById(unknownId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The UUID doesn't match a Teacher.");

        verify(teacherRepository).findById(unknownId);
    }

}
