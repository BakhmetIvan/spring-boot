package mate.academy.library.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import mate.academy.library.dto.book.BookResponseDto;
import mate.academy.library.dto.book.BookSearchParametersDto;
import mate.academy.library.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts =
            "classpath:database/category/add-categories-to-categories-table.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/remove-book-category-from-table.sql",
            "classpath:database/category/remove-categories-from-categories-table.sql",
            "classpath:database/book/remove-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Save a new book")
    public void saveBook_ValidRequest_WillReturnTheBookDto() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
                .setTitle("Test Book")
                .setAuthor("Test Author")
                .setIsbn("978-161-729-045-9")
                .setPrice(BigDecimal.valueOf(100.0))
                .setDescription("Test Description")
                .setCoverImage("Test Cover Image")
                .setCategoryIds(Set.of(1L, 2L));
        BookResponseDto exceptedBookDto = new BookResponseDto()
                .setId(1L)
                .setTitle(createBookRequestDto.getTitle())
                .setAuthor(createBookRequestDto.getAuthor())
                .setIsbn(createBookRequestDto.getIsbn())
                .setPrice(createBookRequestDto.getPrice())
                .setDescription(createBookRequestDto.getDescription())
                .setCoverImage(createBookRequestDto.getCoverImage())
                .setCategoryIds(createBookRequestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookResponseDto actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookResponseDto.class);
        Assertions.assertNotNull(actualBookDto);
        Assertions.assertNotNull(actualBookDto.getId());
        EqualsBuilder.reflectionEquals(exceptedBookDto, actualBookDto, "id");
        EqualsBuilder.reflectionEquals(exceptedBookDto, actualBookDto, "title");
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(scripts = {
            "classpath:database/book/add-books-to-books-table.sql",
            "classpath:database/category/add-categories-to-categories-table.sql",
            "classpath:database/books_categories/add-book-category-to-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/remove-book-category-from-table.sql",
            "classpath:database/category/remove-categories-from-categories-table.sql",
            "classpath:database/book/remove-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get all books")
    void getAll_GivenBooksInCatalog_ShouldReturnAllBooks() throws Exception {
        List<BookResponseDto> expected = new ArrayList<>();
        expected.add(new BookResponseDto()
                .setId(1L)
                .setTitle("Book1")
                .setAuthor("Author1")
                .setIsbn("isbn1")
                .setPrice(BigDecimal.valueOf(10).setScale(2))
                .setDescription("description1")
                .setCoverImage("coverImage1")
                .setCategoryIds(Set.of(1L))
        );
        expected.add(new BookResponseDto()
                .setId(2L)
                .setTitle("Book2")
                .setAuthor("Author2")
                .setIsbn("isbn2")
                .setPrice(BigDecimal.valueOf(20).setScale(2))
                .setDescription("description2")
                .setCoverImage("coverImage2")
                .setCategoryIds(Set.of(1L))
        );

        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookResponseDto> actual = List.of(objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookResponseDto[].class));
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(scripts = {
            "classpath:database/book/add-books-to-books-table.sql",
            "classpath:database/category/add-categories-to-categories-table.sql",
            "classpath:database/books_categories/add-book-category-to-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/remove-book-category-from-table.sql",
            "classpath:database/category/remove-categories-from-categories-table.sql",
            "classpath:database/book/remove-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get book by id")
    void getBookById_ValidId_ShouldReturnValidBookDto() throws Exception {
        Long id = 2L;
        BookResponseDto expectedBookDto = new BookResponseDto()
                .setId(2L)
                .setTitle("Book2")
                .setAuthor("Author2")
                .setIsbn("isbn2")
                .setPrice(BigDecimal.valueOf(20).setScale(2))
                .setDescription("description2")
                .setCoverImage("coverImage2")
                .setCategoryIds(Set.of(1L));

        MvcResult result = mockMvc.perform(
                        get("/books/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookResponseDto actulBookDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class
        );
        Assertions.assertNotNull(actulBookDto);
        EqualsBuilder.reflectionEquals(expectedBookDto, actulBookDto);
        Assertions.assertEquals(expectedBookDto, actulBookDto);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = {
            "classpath:database/book/add-books-to-books-table.sql",
            "classpath:database/category/add-categories-to-categories-table.sql",
            "classpath:database/books_categories/add-book-category-to-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/remove-book-category-from-table.sql",
            "classpath:database/category/remove-categories-from-categories-table.sql",
            "classpath:database/book/remove-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update book by id")
    void update_ValidRequest_ShouldReturnUpdatedBookDto() throws Exception {
        Long id = 2L;
        CreateBookRequestDto updateBookRequestDto = new CreateBookRequestDto()
                .setTitle("Updated Title")
                .setAuthor("Updated Author")
                .setIsbn("Updated isbn")
                .setPrice(BigDecimal.valueOf(100).setScale(2))
                .setDescription("Updated Description")
                .setCoverImage("Updated CoverImage")
                .setCategoryIds(Set.of(1L));
        BookResponseDto expected = new BookResponseDto()
                .setId(id)
                .setTitle(updateBookRequestDto.getTitle())
                .setAuthor(updateBookRequestDto.getAuthor())
                .setIsbn(updateBookRequestDto.getIsbn())
                .setPrice(updateBookRequestDto.getPrice())
                .setDescription(updateBookRequestDto.getDescription())
                .setCoverImage(updateBookRequestDto.getCoverImage())
                .setCategoryIds(updateBookRequestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);
        MvcResult result = mockMvc.perform(
                        put("/books/{id}", id)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class
        );
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = {
            "classpath:database/book/add-books-to-books-table.sql",
            "classpath:database/category/add-categories-to-categories-table.sql",
            "classpath:database/books_categories/add-book-category-to-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/remove-book-category-from-table.sql",
            "classpath:database/category/remove-categories-from-categories-table.sql",
            "classpath:database/book/remove-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete book by id")
    void delete_ValidId_ShouldDeleteCorrectBook() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user")
    @Sql(scripts = {
            "classpath:database/book/add-books-to-books-table.sql",
            "classpath:database/category/add-categories-to-categories-table.sql",
            "classpath:database/books_categories/add-book-category-to-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/remove-book-category-from-table.sql",
            "classpath:database/category/remove-categories-from-categories-table.sql",
            "classpath:database/book/remove-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Search books by parameters")
    void searchBooks_ValidBookSearchParametersDto_ShouldReturnValidBookDto() throws Exception {
        BookResponseDto expected = new BookResponseDto()
                .setId(1L)
                .setTitle("Book1")
                .setAuthor("Author1")
                .setIsbn("isbn1")
                .setPrice(BigDecimal.valueOf(10).setScale(2))
                .setDescription("description1")
                .setCoverImage("coverImage1")
                .setCategoryIds(Set.of(1L));

        BookSearchParametersDto bookSearchParametersDto = new BookSearchParametersDto();
        bookSearchParametersDto.setTitles(new String[]{"Book1"});
        bookSearchParametersDto.setAuthors(new String[]{"Author1"});

        String jsonRequest = objectMapper.writeValueAsString(bookSearchParametersDto);
        MvcResult result = mockMvc.perform(get("/books/search")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookResponseDto> actualList = Arrays.asList(
                objectMapper.readValue(result.getResponse().getContentAsString(), BookResponseDto[].class)
        );
        BookResponseDto actual = actualList.get(0);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
        Assertions.assertEquals(expected, actual);
    }
}
