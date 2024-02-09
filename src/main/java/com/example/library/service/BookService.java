package com.example.library.service;

import com.example.library.model.Book;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    Book save(Book book);

    List findAll();
}
