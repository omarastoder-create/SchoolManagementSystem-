package Rastoder.SchoolManagementSystem.controller;

import Rastoder.SchoolManagementSystem.Controller.ParentController;
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


}
