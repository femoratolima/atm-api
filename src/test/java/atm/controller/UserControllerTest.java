package atm.controller;

import atm.domain.User;
import atm.dto.NameDTO;
import atm.dto.UserAccountDTO;
import atm.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findByEmail() throws Exception {
        when(userService.findById(any())).thenReturn(Optional.of(createUser()));

        mockMvc.perform(get("/api/user/test@test.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email", is("test@test.com")))
                .andExpect(jsonPath("name", is("Test")));
    }

    @Test
    void findAllByEmail() throws Exception {
        when(userService.findAll(anyInt())).thenReturn(createResponse());

        mockMvc.perform(get("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email", is("test@test.com")))
                .andExpect(jsonPath("$.content[0].name", is("Test")));
    }

    @Test
    void create() throws Exception {
        when(userService.create(any())).thenReturn(createUserAccountDTO());

        mockMvc.perform(post("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUser())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("accountId", is("5fc03087-d265-11e7-b8c6-83e29cd24f4c")))
                .andExpect(jsonPath("accountBalance", is(1)))
                .andExpect(jsonPath("email", is("Test@Test")))
                .andExpect(jsonPath("name", is("Test")));
    }

    @Test
    void update() throws Exception {
        when(userService.update(any(), any())).thenReturn(createUser());

        mockMvc.perform(put("/api/user/test@test.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createNameDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email", is("test@test.com")))
                .andExpect(jsonPath("name", is("Test")));
    }

    @Test
    void exclude() throws Exception {
        when(userService.delete(any())).thenReturn(createUser());

        mockMvc.perform(delete("/api/user/test@test.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("Test")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email", is("test@test.com")))
                .andExpect(jsonPath("name", is("Test")));
    }

    private Page<User> createResponse() {
        Pageable pageable = PageRequest.of(0, 20);

        List<User> users = List.of(createUser());

        return new PageImpl<>(users, pageable, users.size());
    }

    private User createUser() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setName("Test");

        return user;
    }

    private UserAccountDTO createUserAccountDTO() {
        UserAccountDTO userAccountDTO = new UserAccountDTO();

        userAccountDTO.setAccountId(UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c"));
        userAccountDTO.setAccountBalance(BigDecimal.ONE);
        userAccountDTO.setName("Test");
        userAccountDTO.setEmail("Test@Test");

        return userAccountDTO;
    }

    private NameDTO createNameDTO() {
        NameDTO nameDTO = new NameDTO();

        nameDTO.setName("Test");

        return nameDTO;
    }

}
