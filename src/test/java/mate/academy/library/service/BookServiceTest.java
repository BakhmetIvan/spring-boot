package mate.academy.library.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import mate.academy.library.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.library.dto.book.BookResponseDto;
import mate.academy.library.dto.book.BookSearchParametersDto;
import mate.academy.library.dto.book.CreateBookRequestDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.mapper.BookMapper;
import mate.academy.library.model.Book;
import mate.academy.library.model.Category;
import mate.academy.library.repository.book.BookRepository;
import mate.academy.library.repository.book.BookSpecificationBuilder;
import mate.academy.library.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final Book FIRST_BOOK = new Book()
            .setId(1L)
            .setTitle("title1")
            .setAuthor("author1")
            .setIsbn("isbn1")
            .setPrice(BigDecimal.valueOf(100))
            .setDescription("description1")
            .setCoverImage("image1")
            .setCategories(Set.of(new Category().setId(1L)
                    .setName("category1")
                    .setDescription("description1")));
    private static final Book SECOND_BOOK = new Book()
            .setId(1L)
            .setTitle("title1")
            .setAuthor("author1")
            .setIsbn("isbn1")
            .setPrice(BigDecimal.valueOf(100))
            .setDescription("description1")
            .setCoverImage("image1")
            .setCategories(Set.of(new Category().setId(1L)
                    .setName("category1")
                    .setDescription("description1")));
    private static final BookResponseDto FIRST_BOOK_RESPONSE_DTO = new BookResponseDto()
            .setId(FIRST_BOOK.getId())
            .setTitle(FIRST_BOOK.getTitle())
            .setDescription(FIRST_BOOK.getDescription())
            .setPrice(FIRST_BOOK.getPrice())
            .setAuthor(FIRST_BOOK.getAuthor())
            .setCoverImage(FIRST_BOOK.getCoverImage())
            .setIsbn(FIRST_BOOK.getIsbn())
            .setCategoryIds(FIRST_BOOK.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
    private static final BookResponseDto SECOND_BOOK_RESPONSE_DTO = new BookResponseDto()
            .setId(SECOND_BOOK.getId())
            .setTitle(SECOND_BOOK.getTitle())
            .setDescription(SECOND_BOOK.getDescription())
            .setPrice(SECOND_BOOK.getPrice())
            .setAuthor(SECOND_BOOK.getAuthor())
            .setCoverImage(SECOND_BOOK.getCoverImage())
            .setIsbn(SECOND_BOOK.getIsbn())
            .setCategoryIds(SECOND_BOOK.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
    private static final CreateBookRequestDto FIRST_CREATE_BOOK_REQUEST_DTO =
            new CreateBookRequestDto()
            .setTitle(FIRST_BOOK.getTitle())
            .setDescription(FIRST_BOOK.getDescription())
            .setPrice(FIRST_BOOK.getPrice())
            .setAuthor(FIRST_BOOK.getAuthor())
            .setCoverImage(FIRST_BOOK.getCoverImage())
            .setIsbn(FIRST_BOOK.getIsbn())
            .setCategoryIds(FIRST_BOOK.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save book with valid data will return the valid bookDto")
    public void saveBook_WithValidData_ShouldReturnValidBookDto() {
        CreateBookRequestDto requestDto = FIRST_CREATE_BOOK_REQUEST_DTO;

        when(bookMapper.toModel(requestDto)).thenReturn(FIRST_BOOK);
        when(bookMapper.toDto(FIRST_BOOK)).thenReturn(FIRST_BOOK_RESPONSE_DTO);
        when(bookRepository.save(FIRST_BOOK)).thenReturn(FIRST_BOOK);
        BookResponseDto actual = bookService.save(requestDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(FIRST_BOOK_RESPONSE_DTO, actual);
        verify(bookMapper, times(1)).toModel(requestDto);
        verify(bookRepository, times(1)).save(FIRST_BOOK);
        verify(bookMapper, times(1)).toDto(FIRST_BOOK);
    }

    @Test
    @DisplayName("Find book by valid id will return the valid book")
    public void findBook_WithValidBookId_ShouldReturnValidBook() {
        Long bookId = 1L;
        BookResponseDto bookResponseDto = FIRST_BOOK_RESPONSE_DTO;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(FIRST_BOOK));
        when(bookMapper.toDto(FIRST_BOOK)).thenReturn(bookResponseDto);
        BookResponseDto actual = bookService.findById(bookId);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bookResponseDto, actual);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toDto(FIRST_BOOK);
    }

    @Test
    @DisplayName("Find book by invalid id will throw the exception")
    public void findBook_WithInvalidBookId_ShouldThrowException() {
        Long bookId = 100L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        String actual = Assertions.assertThrows(EntityNotFoundException.class,
                (() -> bookService.findById(bookId))).getMessage();
        String expected = "Can't find book by id: " + bookId;
        Assertions.assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    @DisplayName("Find all books will return the list of books")
    public void findAllBooks_ShouldReturnListOfBooks() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(FIRST_BOOK);
        bookList.add(SECOND_BOOK);
        List<BookResponseDto> bookResponseDtoList = new ArrayList<>();
        bookResponseDtoList.add(FIRST_BOOK_RESPONSE_DTO);
        bookResponseDtoList.add(SECOND_BOOK_RESPONSE_DTO);
        Page<Book> bookPage = new PageImpl<>(bookList);

        when(bookRepository.findAll(Pageable.unpaged())).thenReturn(bookPage);
        when(bookMapper.toDto(bookList.get(0))).thenReturn(bookResponseDtoList.get(0));
        when(bookMapper.toDto(bookList.get(1))).thenReturn(bookResponseDtoList.get(1));
        List<BookResponseDto> actual = bookService.findAll(Pageable.unpaged());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bookResponseDtoList, actual);
        verify(bookRepository, times(1)).findAll(Pageable.unpaged());
        verify(bookMapper, times(1)).toDto(bookList.get(0));
        verify(bookMapper, times(1)).toDto(bookList.get(1));
    }

    @Test
    @DisplayName("Update book by valid id will return the updated book")
    public void updateBookById_WithValidId_ShouldReturnUpdatedBook() {
        Long bookId = 1L;
        Book oldBook = FIRST_BOOK;
        Book updatedBook = FIRST_BOOK.setTitle("Updated title");
        CreateBookRequestDto updateRequestBookDto =
                FIRST_CREATE_BOOK_REQUEST_DTO.setTitle("Updated title");
        BookResponseDto updatedBookDto = FIRST_BOOK_RESPONSE_DTO.setTitle("Updated title");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(oldBook));
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDto);
        BookResponseDto actual = bookService.updateBookInfoById(bookId, updateRequestBookDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(updatedBookDto, actual);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(updatedBook);
        verify(bookMapper, times(1)).toDto(updatedBook);
    }

    @Test
    @DisplayName("Update book by invalid id will produce an exception")
    public void updateBookById_WithInvalidId_ShouldThrowException() {
        Long bookId = 100L;
        CreateBookRequestDto updateRequestBookDto =
                FIRST_CREATE_BOOK_REQUEST_DTO.setTitle("Updated title");

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        String actual = Assertions.assertThrows(EntityNotFoundException.class,
                (() -> bookService.updateBookInfoById(bookId, updateRequestBookDto))).getMessage();
        String expected = "Can't find book by id " + bookId;
        Assertions.assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    @DisplayName("Delete book by valid id will delete the book from db")
    public void deleteBookById_WithValidId_ShouldDeleteBook() {
        Long bookId = 1L;

        bookService.deleteById(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Search book by valid search parameters will return the list of books")
    public void searchBookByTitleAndAuthor_WithValidData_ShouldReturnBookList() {
        BookSearchParametersDto searchParameters = new BookSearchParametersDto()
                .setAuthors(new String[]{"Author"}).setTitles(new String[]{"Title"});
        List<Book> bookList = new ArrayList<>();
        bookList.add(FIRST_BOOK.setTitle("Title").setAuthor("Author"));
        bookList.add(SECOND_BOOK.setTitle("Title").setAuthor("Author"));
        List<BookResponseDto> responseDtoList = new ArrayList<>();
        responseDtoList.add(FIRST_BOOK_RESPONSE_DTO.setTitle("Title").setAuthor("Author"));
        responseDtoList.add(SECOND_BOOK_RESPONSE_DTO.setTitle("Title").setAuthor("Author"));
        Specification<Book> bookSpecification =
                (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Page<Book> bookPage = new PageImpl<>(bookList);

        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, Pageable.unpaged())).thenReturn(bookPage);
        when(bookMapper.toDto(bookList.get(0))).thenReturn(responseDtoList.get(0));
        when(bookMapper.toDto(bookList.get(1))).thenReturn(responseDtoList.get(1));
        List<BookResponseDto> actual = bookService.search(searchParameters, Pageable.unpaged());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(responseDtoList, actual);
        verify(bookRepository, times(1)).findAll(bookSpecification, Pageable.unpaged());
        verify(bookMapper, times(1)).toDto(bookList.get(0));
        verify(bookMapper, times(1)).toDto(bookList.get(1));
    }

    @Test
    @DisplayName("Search book by invalid search parameters will return an empty list")
    public void searchBookByTitleAndAuthor_WithInvalidData_ShouldReturnEmptyList() {
        // Arrange
        BookSearchParametersDto searchParameters = new BookSearchParametersDto()
                .setAuthors(new String[]{"No exist Author"}).
                setTitles(new String[]{"No exist Title"});

        List<Book> emptyBookList = new ArrayList<>();
        List<BookResponseDto> emptyResponseDtoList = new ArrayList<>();
        Specification<Book> bookSpecification =
                (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Page<Book> emptyBookPage = new PageImpl<>(emptyBookList);

        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, Pageable.unpaged())).thenReturn(emptyBookPage);

        List<BookResponseDto> actual = bookService.search(searchParameters, Pageable.unpaged());

        Assertions.assertEquals(emptyResponseDtoList, actual);
        verify(bookRepository, times(1)).findAll(bookSpecification, Pageable.unpaged());
        verify(bookMapper, times(0)).toDto(any(Book.class));
    }

    @Test
    @DisplayName("Find books by valid category id will return the list of books")
    public void findAllByCategoriesId_WithValidId_ShouldReturnBookList() {
        Long categoryId = 1L;
        List<Book> bookList = new ArrayList<>();
        bookList.add(FIRST_BOOK);
        bookList.add(SECOND_BOOK);
        List<BookDtoWithoutCategoryIds> responseDtoList = new ArrayList<>();
        responseDtoList.add(new BookDtoWithoutCategoryIds()
                .setId(FIRST_BOOK.getId())
                .setAuthor(FIRST_BOOK.getAuthor())
                .setIsbn(FIRST_BOOK.getIsbn())
                .setPrice(FIRST_BOOK.getPrice())
                .setCoverImage(FIRST_BOOK.getCoverImage())
                .setDescription(FIRST_BOOK.getDescription()));
        responseDtoList.add(new BookDtoWithoutCategoryIds()
                .setId(SECOND_BOOK.getId())
                .setAuthor(SECOND_BOOK.getAuthor())
                .setIsbn(SECOND_BOOK.getIsbn())
                .setPrice(SECOND_BOOK.getPrice())
                .setCoverImage(SECOND_BOOK.getCoverImage())
                .setDescription(SECOND_BOOK.getDescription()));

        when(bookRepository.findAllByCategoriesId(categoryId,
                Pageable.unpaged())).thenReturn(bookList);
        when(bookMapper.toDtoWithoutCategories(bookList.get(0)))
                .thenReturn(responseDtoList.get(0));
        when(bookMapper.toDtoWithoutCategories(bookList.get(1)))
                .thenReturn(responseDtoList.get(1));
        List<BookDtoWithoutCategoryIds> actual =
                bookService.findAllByCategoriesId(categoryId, Pageable.unpaged());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(responseDtoList, actual);
        verify(bookRepository, times(1)).findAllByCategoriesId(categoryId, Pageable.unpaged());
        verify(bookMapper, times(1)).toDtoWithoutCategories(bookList.get(0));
        verify(bookMapper, times(1)).toDtoWithoutCategories(bookList.get(1));
    }

    @Test
    @DisplayName("Find books by invalid category id will return an empty list")
    public void findAllByCategoriesId_WithInvalidId_ShouldReturnEmptyList() {
        Long invalidCategoryId = 999L;
        List<Book> emptyBookList = new ArrayList<>();
        List<BookDtoWithoutCategoryIds> emptyResponseDtoList = new ArrayList<>();

        when(bookRepository.findAllByCategoriesId(invalidCategoryId,
                Pageable.unpaged())).thenReturn(emptyBookList);
        List<BookDtoWithoutCategoryIds> actual =
                bookService.findAllByCategoriesId(invalidCategoryId, Pageable.unpaged());

        Assertions.assertEquals(emptyResponseDtoList, actual);
        verify(bookRepository, times(1))
                .findAllByCategoriesId(invalidCategoryId, Pageable.unpaged());
        verify(bookMapper, never()).toDtoWithoutCategories(any(Book.class));
    }
}
