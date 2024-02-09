package com.example.library.repository;

import com.example.library.model.Book;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository {
    Book save(Book book);

    List findAll();
}
