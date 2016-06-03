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

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static com.sun.tools.doclint.Entity.and;
import static com.sun.tools.doclint.Entity.not;
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

    @Autowired
    BookRepository bookRepository;

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

    @Autowired
    private EntityManager em;

    @Transactional
    @Test
    public void getAuthors_returnsAllAuthorsAndBooksForEachAuthor() throws Exception {

        Author author = new Author();
        authorRepository.save(author);

        Book book = new Book();
        List<Book> books = Arrays.asList(book);
        bookRepository.save(books);

        book.setAuthor(author);
        bookRepository.save(book);

        // TODO
        // The problem we were having was due to a few things:
//        1. lazy evaluation and each db call was it's own session, so we couldn't get things which were lazy evaluated when after the fact (you can fix this by making things eager, but that's not a great solution
//        2. not updating both sides of the relationship in the code (so code objects were inconsistent)
//        3. not being able to access the updated data with mockMVC because it might not be saved until the end of the transaction
//
//        Solution:
//        Don't depend on being able to re-read data from a database mid-transaction (it's held in cache and doesn't update there)
//        Make sure you only do jpa stuff in transactions (so you don't have sessions closing without you realising)

//        Plan: have data creation and saving in a separate before method, which will be transactional

//
//        author = null;
//
//        bookRepository.flush();
//        authorRepository.flush();

//        authorRepository.flush();

//        em.flush();
//        em.clear();


        book = bookRepository.findAll().get(0);
        author = authorRepository.findAll().get(0);
        System.out.println("====================");
        System.out.println(book.toString());
        System.out.println("--------------------");
        System.out.println(author.toString());
        System.out.println("--------------------");
//        System.out.println(author.getBooks());
        System.out.println("--------------------");
        System.out.println(book.getAuthor().toString());
        System.out.println("====================");

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].books", hasSize(1)))
                .andReturn();
    }
}
