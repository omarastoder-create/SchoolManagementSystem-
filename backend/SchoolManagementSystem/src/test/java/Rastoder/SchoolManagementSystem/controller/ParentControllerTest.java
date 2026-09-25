package Rastoder.SchoolManagementSystem.controller;

import Rastoder.SchoolManagementSystem.dto.ParentRequest;
import Rastoder.SchoolManagementSystem.dto.ParentResponse;
import Rastoder.SchoolManagementSystem.service.ParentService;
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
import java.util.UUID;

@WebMvcTest(ParentController.class)
public class ParentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ParentService parentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateParentAndStatus201() throws Exception{

        ParentRequest request  = new ParentRequest(
                "Celo",
                "avdic",
                "mengmail@gmail.com",
                "691925956"

        );

        UUID parentId = UUID.randomUUID();
        ParentResponse mockResponse = new ParentResponse(
                parentId,
                "Celo",
                "avdic",
                "mengmail@gmail.com",
                "691925956",
                true

        );

        Mockito.when(parentService.createParent(Mockito.any())).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/parents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Celo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("avdic"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("mengmail@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("691925956"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(true));
    }

    @Test
    void shouldArchiveParentAndReturnStatus200()throws Exception{
        //Arrange
        UUID parentId = UUID.randomUUID();

        ParentResponse mockResponse =  new ParentResponse(
                parentId,
                "Ernad",
                "Rastoder",
                "ernad@mail.cool",
                "6666666567",
                false
        );

        Mockito.when(parentService.setParentToUnactive(parentId)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/parents/"+parentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(false));
    }

    @Test
    void getParentWithId() throws Exception {
        UUID parentId = UUID.randomUUID();

        ParentResponse mockResponse =  new ParentResponse(
                parentId,
                "Ernad",
                "Rastoder",
                "ernad@mail.cool",
                "6666666567",
                false
        );

        Mockito.when(parentService.findById(parentId)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/parents/"+parentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.email").value("ernad@mail.cool"))
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.parentId").value(parentId.toString()));;
    }


    @Test
    void updateParentWithId() throws Exception {
        UUID parentId = UUID.randomUUID();

        ParentResponse mockResponse =  new ParentResponse(
                parentId,
                "Ernad",
                "Rastoder",
                "ernad@mail.cool",
                "6666666567",
                true
        );

        ParentRequest mockRequest = new ParentRequest(
                "Ernad",
                "Rastoder",
                "ernad@mail.cool",
                "6666666567"
        );

        Mockito.when(parentService.updateParentWithId(parentId,mockRequest)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/parents/" + parentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.parentId").value(parentId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ernad@mail.cool"));
    }

    @Test
    void getAllInactiveParents() throws Exception {
        UUID parentId = UUID.randomUUID();

        ParentResponse mockResponse =  new ParentResponse(
                parentId,
                "Ernad",
                "Rastoder",
                "ernad@mail.cool",
                "6666666567",
                false
        );
        Mockito.when(parentService.getAllInactiveParents()).thenReturn(List.of(mockResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/parents/archived"))
                .andExpect(MockMvcResultMatchers.status().isOk()) // this is why we do it
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isActive").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].parentId").value(parentId.toString()));
    }
}
