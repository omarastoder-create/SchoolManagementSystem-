package Rastoder.SchoolManagementSystem.controller;

import Rastoder.SchoolManagementSystem.Controller.TeacherController;
import Rastoder.SchoolManagementSystem.dto.TeacherRequest;
import Rastoder.SchoolManagementSystem.dto.TeacherResponse;
import Rastoder.SchoolManagementSystem.model.Language;
import Rastoder.SchoolManagementSystem.service.TeacherService;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@WebMvcTest(TeacherController.class)
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TeacherService teacherService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addTeacher() throws Exception {
        UUID teacherId = UUID.randomUUID();

        Set<Language> languages = new HashSet<>();
        languages.add(Language.BOSNIAN);

        TeacherResponse mockResponse = new TeacherResponse(
                teacherId,
                "Joe",
                "Biden",
                "444555666",
                "Bosnistika",
                languages,
                true
        );
        TeacherRequest mockRequest = new TeacherRequest(
                "Joe",
                "Biden",
                "444555666",
                "Bosnistika",
                languages
        );

        Mockito.when(teacherService.createTeacher(Mockito.any(TeacherRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teacherId").value(teacherId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Joe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.familyName").value("Biden"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("444555666"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Bosnistika"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(true));
    }

    @Test
    void getAllTeachers() throws Exception {
        UUID teacherId = UUID.randomUUID();

        Set<Language> languages = new HashSet<>();
        languages.add(Language.BOSNIAN);

        TeacherResponse mockResponse = new TeacherResponse(
                teacherId,
                "Joe",
                "Biden",
                "444555666",
                "Bosnistika",
                languages,
                true
        );

        Mockito.when(teacherService.getAllActiveTeachers()).thenReturn(List.of(mockResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teachers"))
                .andExpect(MockMvcResultMatchers.status().isOk()) // checking the handshake
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].teacherId").value(teacherId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Joe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isActive").value(true));
    }
    @Test
    void getTeacherById() throws Exception{
        UUID teacherId = UUID.randomUUID();

        Set<Language> languages = new HashSet<>();
        languages.add(Language.BOSNIAN);

        TeacherResponse mockResponse = new TeacherResponse(
                teacherId,
                "Joe",
                "Biden",
                "444555666",
                "Bosnistika",
                languages,
                true
        );

        Mockito.when(teacherService.getTeacherById(teacherId)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teachers/"+teacherId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teacherId").value(teacherId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Joe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.familyName").value("Biden"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("444555666"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Bosnistika"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(true));

    }
    @Test
    void updateTeacherWithId() throws Exception{
        UUID teacherId = UUID.randomUUID();

        Set<Language> languages = new HashSet<>();
        languages.add(Language.BOSNIAN);

        TeacherResponse mockResponse = new TeacherResponse(
                teacherId,
                "Joe",
                "Biden",
                "444555666",
                "Bosnistika",
                languages,
                true
        );
        TeacherRequest mockRequest = new TeacherRequest(
                "Joe",
                "Biden",
                "444555666",
                "Bosnistika",
                languages
        );


        Mockito.when(teacherService.updateTeacherWithId(teacherId,mockRequest)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/teachers/"+teacherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teacherId").value(teacherId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Joe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.familyName").value("Biden"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("444555666"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Bosnistika"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(true));

    }
    @Test
    void updateTeacherToInactive() throws Exception{
        UUID teacherId = UUID.randomUUID();

        Set<Language> languages = new HashSet<>();
        languages.add(Language.BOSNIAN);

        TeacherResponse mockResponse = new TeacherResponse(
                teacherId,
                "Joe",
                "Biden",
                "444555666",
                "Bosnistika",
                languages,
                false
        );

        Mockito.when(teacherService.updateTeacherToInactive(teacherId)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/teachers/"+teacherId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teacherId").value(teacherId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Joe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.familyName").value("Biden"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("444555666"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Bosnistika"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(false));

    }
    @Test
    void getAllInactiveTeachers() throws Exception{
        UUID teacherId = UUID.randomUUID();

        Set<Language> languages = new HashSet<>();
        languages.add(Language.BOSNIAN);

        TeacherResponse mockResponse = new TeacherResponse(
                teacherId,
                "Joe",
                "Biden",
                "444555666",
                "Bosnistika",
                languages,
                false
        );

        Mockito.when(teacherService.getAllInactiveTeachers()).thenReturn(List.of(mockResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teachers/archived"))
                .andExpect(MockMvcResultMatchers.status().isOk()) // checking the handshake
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].teacherId").value(teacherId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Joe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isActive").value(false));
    }

}
