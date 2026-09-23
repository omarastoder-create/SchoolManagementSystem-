package Rastoder.SchoolManagementSystem.controller;

import Rastoder.SchoolManagementSystem.Controller.StudentGroupController;
import Rastoder.SchoolManagementSystem.dto.StudentGroupRequest;
import Rastoder.SchoolManagementSystem.dto.StudentGroupResponse;
import Rastoder.SchoolManagementSystem.model.Room;
import Rastoder.SchoolManagementSystem.service.StudentGroupService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@WebMvcTest(StudentGroupController.class)
public class StudentGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentGroupService studentGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createStudentGroup() throws Exception {
        // Arrange
        UUID teacherId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        Set<UUID> studentIds = Set.of(studentId);
        Set<UUID> sessions = Set.of(sessionId);

        StudentGroupRequest mockRequest = new StudentGroupRequest(
                Room.ROOM_1,
                teacherId,
                studentIds
        );

        StudentGroupResponse mockResponse = new StudentGroupResponse(
                groupId,
                Room.ROOM_1,
                sessions,
                teacherId,
                studentIds
        );

        Mockito.when(studentGroupService.createStudentGroup(mockRequest)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/studentGroups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupId").value(groupId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.room").value(Room.ROOM_1.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.teacherId").value(teacherId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.studentsIds").value(studentId.toString()));
    }

    @Test
    void addStudentToGroup() throws Exception {
        UUID groupId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        Mockito.doNothing().when(studentGroupService).addStudentToStudentGroup(studentId, groupId);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/studentGroups/" + groupId + "/student/" + studentId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(studentGroupService, Mockito.times(1)).addStudentToStudentGroup(studentId, groupId);
    }

    @Test
    void getAllStudents() throws Exception {
        UUID groupId = UUID.randomUUID();
        UUID teacherId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        StudentGroupResponse mockResponse = new StudentGroupResponse(
                groupId,
                Room.ROOM_1,
                Set.of(sessionId),
                teacherId,
                Set.of(studentId)
        );

        Mockito.when(studentGroupService.getAllStudentGroups()).thenReturn(List.of(mockResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/studentGroups"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].groupId").value(groupId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].room").value(Room.ROOM_1.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].teacherId").value(teacherId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studentsIds[0]").value(studentId.toString()));
    }

    @Test
    void getAllStudentsGroupById() throws Exception {
        UUID groupId = UUID.randomUUID();
        UUID teacherId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        StudentGroupResponse mockResponse = new StudentGroupResponse(
                groupId,
                Room.ROOM_1,
                Set.of(sessionId),
                teacherId,
                Set.of(studentId)
        );

        Mockito.when(studentGroupService.getStudentGroupByService(groupId)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/studentGroups/" + groupId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupId").value(groupId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.room").value(Room.ROOM_1.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.teacherId").value(teacherId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.studentsIds[0]").value(studentId.toString()));
    }

    @Test
    void assignNewTeacher() throws Exception {
        UUID groupId = UUID.randomUUID();
        UUID teacherId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        StudentGroupResponse mockResponse = new StudentGroupResponse(
                groupId,
                Room.ROOM_1,
                Set.of(sessionId),
                teacherId,
                Set.of(studentId)
        );

        Mockito.when(studentGroupService.assignTeacher(groupId, teacherId)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/studentGroups/" + groupId + "/teacher/" + teacherId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupId").value(groupId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.teacherId").value(teacherId.toString()));
    }

    @Test
    void getAllGroupsForTeacher() throws Exception {
        UUID groupId = UUID.randomUUID();
        UUID teacherId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        StudentGroupResponse mockResponse = new StudentGroupResponse(
                groupId,
                Room.ROOM_1,
                Set.of(sessionId),
                teacherId,
                Set.of(studentId)
        );

        Mockito.when(studentGroupService.getStudentGroupByTeacherId(teacherId)).thenReturn(List.of(mockResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/studentGroups/teacher/" + teacherId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].groupId").value(groupId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].teacherId").value(teacherId.toString()));
    }
}
