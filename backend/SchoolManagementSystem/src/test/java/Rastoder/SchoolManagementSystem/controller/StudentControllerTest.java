package Rastoder.SchoolManagementSystem.controller;

import Rastoder.SchoolManagementSystem.Controller.StudentController;
import Rastoder.SchoolManagementSystem.dto.StudentRequest;
import Rastoder.SchoolManagementSystem.dto.StudentResponse;
import Rastoder.SchoolManagementSystem.model.Language;
import Rastoder.SchoolManagementSystem.service.StudentService;
import org.hamcrest.Matchers;
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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateStudentWithParentsAndReturnStatus201() throws Exception {

        // 1. ARRANGE
        UUID fatherId = UUID.randomUUID();
        UUID motherId = UUID.randomUUID();
        Set<UUID> parentIds = Set.of(fatherId, motherId);

        StudentRequest request = new StudentRequest(
                "Joe",
                "Doe",
                LocalDate.of(2010, 5, 15),
                3,
                Language.BOSNIAN,
                parentIds
        );

        StudentResponse mockResponse = new StudentResponse(
                UUID.randomUUID(),
                "Joe",
                "Doe",
                LocalDate.of(2010, 5, 15),
                3,
                Language.BOSNIAN,
                parentIds,
                true
        );

        Mockito.when(studentService.createStudent(Mockito.any())).thenReturn(mockResponse);

        // 2. ACT & ASSERT
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Joe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.language").value("BOSNIAN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parentIds").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.parentIds.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parentIds",
                        Matchers.hasItems(fatherId.toString(), motherId.toString())));
    }

    @Test
    void shouldArchiveStudentAndReturnStatus200() throws Exception {

        // 1. ARRANGE
        UUID studentId = UUID.randomUUID();

        StudentResponse mockResponse = new StudentResponse(
                studentId,
                "Joe",
                "Doe",
                LocalDate.of(2010, 5, 15),
                3,
                Language.BOSNIAN,
                new HashSet<>(),
                false
        );

        // Wir prüfen strikt, ob die korrekte ID übergeben wird
        Mockito.when(studentService.archiveStudentWithId(studentId)).thenReturn(mockResponse);

        // 2. ACT & ASSERT (Kein Body, kein ObjectMapper, nur URL mit ID)
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/" + studentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(false));
    }

    @Test
    void shouldLevelUpAStudent() throws Exception {
        UUID studentId = UUID.randomUUID();

        StudentResponse mockResponse = new StudentResponse(
                studentId,
                "Joe",
                "Doe",
                LocalDate.of(2010, 5, 15),
                3,
                Language.BOSNIAN,
                new HashSet<>(),
                false
        );

        Mockito.when(studentService.addStudentLevel(studentId)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/students/addGrade/"+studentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.level").value(3));
    }
}
