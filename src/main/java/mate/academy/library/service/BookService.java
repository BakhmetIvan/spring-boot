package mate.academy.library.service;

import mate.academy.library.dto.book.BookDto;
import mate.academy.library.dto.book.BookSearchParametersDto;
import mate.academy.library.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateBookInfoById(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable);
}
