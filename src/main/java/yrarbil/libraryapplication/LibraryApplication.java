package yrarbil.libraryapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import yrarbil.libraryapplication.model.*;
import yrarbil.libraryapplication.model.enums.BookStatus;
import yrarbil.libraryapplication.model.enums.Format;
import yrarbil.libraryapplication.model.enums.Genre;
import yrarbil.libraryapplication.model.enums.LoanStatus;
import yrarbil.libraryapplication.service.*;

import java.time.LocalDate;
import java.util.ArrayList;


@SpringBootApplication
public class LibraryApplication {

    private final AuthorService authorService;
    private final BookService bookService;
    private final LoanService loanService;
    private final PatronService patronService;
    private final PublisherService publisherService;

    @Autowired
    public LibraryApplication(AuthorService authorService, BookService bookService, LoanService loanService, PatronService patronService, PublisherService publisherService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.loanService = loanService;
        this.patronService = patronService;
        this.publisherService = publisherService;
    }

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {

            Publisher publisher1 = new Publisher("publisher name", new ArrayList<>(), "address", "website");
            publisherService.save(publisher1);

            Author author1 = new Author("author", LocalDate.parse("1994-12-23"), "bio", new ArrayList<>());
            authorService.save(author1);

            Book book1 = new Book(publisher1, "title", author1, Genre.BIOGRAPHY, Format.DIGITAL, 220, BookStatus.BORROWED);
            bookService.save(book1);

            Patron patron1 = new Patron("username", "pw", "name", 22, new ArrayList<>());
            patronService.save(patron1);

            Loan loan1 = new Loan(patron1, LocalDate.now(), LocalDate.now().plusDays(30), null, LoanStatus.ACTIVE);
            loanService.save(loan1);



        };
    }
}
