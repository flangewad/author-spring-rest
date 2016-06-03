package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)

//TODO why did we have to replace this with the two annotations underneath it?
@SpringApplicationConfiguration(classes = Application.class) // use this to load context for ITs. It loads the same context that @SpringBootApplication is loading
@WebAppConfiguration // if we want to have an autowired webAppContext
//@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class) // if we used this, it would load the context from the classes we specify as arguments
public class AuthorsEndpointIT {

    @Autowired
    WebApplicationContext webAppContext;

    @Autowired
    AuthorRepository authorRepository;

    MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webAppContext).build();
    } // this is integration-style mockMVC
    // it loads the context for everything, so can send requests to any controller

    // sending a get request to the author endpoint should return a list of authors
    @Test
    public void whenAuthorsListIsEmpty_responseIsEmpty() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void whenAuthorsListIsPolulated_responseReturnsTheAuthors() throws Exception {

        List<Author> authorList = Arrays.asList(new Author(), new Author());
        authorRepository.save(authorList);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }
}
