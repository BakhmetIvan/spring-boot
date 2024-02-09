package com.example.library;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApplication {
    private BookService bookService;

    @Autowired
    public LibraryApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Witch 3");
            book.setPrice(BigDecimal.valueOf(100));
            book.setIsbn("nnn");
            book.setAuthor("Nicola Bargy");
            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }
}
