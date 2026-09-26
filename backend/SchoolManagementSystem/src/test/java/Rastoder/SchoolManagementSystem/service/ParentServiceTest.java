package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.ParentRequest;
import Rastoder.SchoolManagementSystem.dto.ParentResponse;
import Rastoder.SchoolManagementSystem.model.Parent;
import Rastoder.SchoolManagementSystem.repository.ParentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ParentServiceTest {

    @Mock
    private ParentRepository parentRepository;

    @InjectMocks
    private ParentService underTest;

    @Captor
    private ArgumentCaptor<Parent> parentCaptor;

    private Parent dummyParent;

    @BeforeEach
    void setUp(){
        UUID parentId = UUID.randomUUID();
        dummyParent = new Parent(
                "Joe",
                "Hansen",
                "hansen@g.com",
                "123123123"
        );

        dummyParent.setParentId(parentId);
    }

    @Test
    void createParent_shouldReturnCreatedParent(){
        //Arrange
        ParentRequest request = new ParentRequest(
                "Joe",
                "Hansen",
                "hansen@g.com",
                "123123123"
        );

        when(parentRepository.save(any(Parent.class))).thenReturn(dummyParent);
        //Act

        ParentResponse response = underTest.createParent(request);

        //Assert
        verify(parentRepository).save(parentCaptor.capture());
        Parent capturedParent = parentCaptor.getValue();


        assertThat(response.email()).isEqualTo("hansen@g.com");
        assertThat(response.name()).isEqualTo("Joe");
        assertThat(response.phoneNumber()).isEqualTo("123123123");
        assertThat(response.surname()).isEqualTo("Hansen");
        assertThat(response.isActive()).isTrue();
        assertThat(response.parentId()).isEqualTo(dummyParent.getParentId());

        assertThat(capturedParent.getEmail()).isEqualTo("hansen@g.com");
        assertThat(capturedParent.getName()).isEqualTo("Joe");
        assertThat(capturedParent.getSurname()).isEqualTo("Hansen");
        assertThat(capturedParent.getPhoneNumber()).isEqualTo("123123123");
        assertThat(capturedParent.isActive()).isTrue();
    }

    @Test
    void findById_shouldReturnParentWithId() {
        //Arrange
        UUID parentId = UUID.randomUUID();
        dummyParent.setParentId(parentId);

        when(parentRepository.findById(parentId)).thenReturn(Optional.of(dummyParent));
        //Act

        ParentResponse response = underTest.findById(parentId);
        //Assert

        verify(parentRepository).findById(parentId);

        assertThat(response.email()).isEqualTo("hansen@g.com");
        assertThat(response.name()).isEqualTo("Joe");
        assertThat(response.phoneNumber()).isEqualTo("123123123");
        assertThat(response.surname()).isEqualTo("Hansen");
        assertThat(response.isActive()).isTrue();
        assertThat(response.parentId()).isEqualTo(dummyParent.getParentId());
    }

    @Test
    void findById_shouldNotReturnParentWithId(){
        //Arrange
        UUID falseId = UUID.randomUUID();

        when(parentRepository.findById(falseId)).thenReturn(Optional.empty());
        //Act and Assert
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> underTest.findById(falseId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Parent with the id ("+falseId+") not found.");

        verify(parentRepository).findById(falseId);
    }

    @Test
    void updateParentWithId_shouldUpdateParentWithId(){
        //Arrange
        UUID parentId = dummyParent.getParentId();
        ParentRequest request = new ParentRequest(
                "Joey",
                "Doey",
                "g@g.com",
                "123123321"
        );

        when(parentRepository.findById(parentId)).thenReturn(Optional.of(dummyParent));
        when(parentRepository.save(any(Parent.class))).thenReturn(dummyParent);
        //Act
        ParentResponse response = underTest.updateParentWithId(parentId,request);

        verify(parentRepository).save(parentCaptor.capture());
        Parent capturedParent = parentCaptor.getValue();

        assertThat(capturedParent.getParentId()).isEqualTo(parentId);
        assertThat(capturedParent.getName()).isEqualTo("Joe");
        assertThat(capturedParent.getSurname()).isEqualTo("Hansen");
        assertThat(capturedParent.getEmail()).isEqualTo("g@g.com");
        assertThat(capturedParent.getPhoneNumber()).isEqualTo("123123321");
        assertThat(capturedParent.isActive()).isTrue();

        assertThat(response.parentId()).isEqualTo(parentId);
        assertThat(response.name()).isEqualTo("Joe");
        assertThat(response.surname()).isEqualTo("Hansen");
        assertThat(response.email()).isEqualTo("g@g.com");
        assertThat(response.phoneNumber()).isEqualTo("123123321");
        assertThat(response.isActive()).isTrue();
    }

    @Test
    void setParentToUnactive_shouldReturnUnactiveUser(){
        //Arrange
        UUID parentId = dummyParent.getParentId();

        when(parentRepository.findById(parentId)).thenReturn(Optional.of(dummyParent));
        when(parentRepository.save(any(Parent.class))).thenReturn(dummyParent);

        //Act
        ParentResponse response = underTest.setParentToUnactive(parentId);

        verify(parentRepository).save(parentCaptor.capture());
        Parent capturedParent = parentCaptor.getValue();

        assertThat(response.isActive()).isFalse();
        assertThat(capturedParent.isActive()).isFalse();
        verify(parentRepository, never()).delete(any());
    }

    @Test
    void getAllInactiveParents_shouldReturnAllInactiveParents(){
        //Arrange
        dummyParent.setActive(false);
        when(parentRepository.findByIsActiveFalse()).thenReturn(List.of(dummyParent));
        //Act
        List<ParentResponse> parents =underTest.getAllInactiveParents();
        //Assert
        verify(parentRepository).findByIsActiveFalse();

        assertThat(parents).hasSize(1);
        assertThat(parents.get(0).isActive()).isFalse();// false is the basic value of a boolean so testing other values is needed
        assertThat(parents.get(0).name()).isEqualTo("Joe");
        assertThat(parents.get(0).parentId()).isEqualTo(dummyParent.getParentId());
    }
    @Test
    void updateParentWithId_shouldKeepOldValues_whenRequestFieldsAreNull() {
        // Arrange
        UUID parentId = dummyParent.getParentId();
        ParentRequest emptyRequest = new ParentRequest(
                null,
                null,
                null,
                null
        );

        when(parentRepository.findById(parentId)).thenReturn(Optional.of(dummyParent));
        when(parentRepository.save(any(Parent.class))).thenReturn(dummyParent);

        // Act
        ParentResponse response = underTest.updateParentWithId(parentId, emptyRequest);

        // Assert
        verify(parentRepository).findById(parentId);
        verify(parentRepository).save(parentCaptor.capture());
        Parent capturedParent = parentCaptor.getValue();

        assertThat(capturedParent.getParentId()).isEqualTo(parentId);
        assertThat(capturedParent.getName()).isEqualTo("Joe");
        assertThat(capturedParent.getSurname()).isEqualTo("Hansen");
        assertThat(capturedParent.getEmail()).isEqualTo("hansen@g.com");
        assertThat(capturedParent.getPhoneNumber()).isEqualTo("123123123");
        assertThat(capturedParent.isActive()).isTrue();

        assertThat(response.email()).isEqualTo("hansen@g.com");
        assertThat(response.phoneNumber()).isEqualTo("123123123");
    }
}
