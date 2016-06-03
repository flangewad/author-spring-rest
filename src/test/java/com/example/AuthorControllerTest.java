package com.example;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthorControllerTest {

    private AuthorController authorController;
    private AuthorService authorService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        authorService = mock(AuthorService.class);

        authorController = new AuthorController(authorService);

        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build(); // this is the unit-level mockMVC
        // it can only send requests to the controller you pass in on setup.
    }

    @Test
    public void getAuthorsReturnsAuthorsFromService() throws Exception{
        List<Author> authorsAndTheirBooks = new ArrayList<>();

        when(authorService.getAuthorsAndTheirBooks()).thenReturn(authorsAndTheirBooks);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk());

        verify(authorService).getAuthorsAndTheirBooks();
    }

}
