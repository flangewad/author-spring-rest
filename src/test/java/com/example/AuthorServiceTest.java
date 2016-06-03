package com.example;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AuthorServiceTest {
    private AuthorService authorService;
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;

    @Before
    public void setUp() {
        authorRepository = Mockito.mock(AuthorRepository.class);
        bookRepository = Mockito.mock(BookRepository.class);
        authorService = new AuthorService(authorRepository, bookRepository);
    }

    @Test
    public void getAuthorsReturnsFindAllFromAuthorRepository() {

        List allAuthors = Mockito.mock(List.class);
        Mockito.when(authorRepository.findAll()).thenReturn(allAuthors);
        List booksByAuthor = Mockito.mock(List.class);
//        Mockito.when(bookRepository.findByAuthor()).thenReturn(booksByAuthor);



        assertThat(authorService.getAuthorsAndTheirBooks(), is(allAuthors));

    }

}
