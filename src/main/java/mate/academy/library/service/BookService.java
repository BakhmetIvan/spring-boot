package mate.academy.library.service;

import mate.academy.library.dto.BookDto;
import mate.academy.library.dto.CreateBookRequestDto;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
