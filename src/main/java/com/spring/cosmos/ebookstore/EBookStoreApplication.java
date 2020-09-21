package com.spring.cosmos.ebookstore;

import com.spring.cosmos.ebookstore.model.book.Book;
import com.spring.cosmos.ebookstore.model.book.BookRepository;
import com.spring.cosmos.ebookstore.model.book.BookRepositoryAsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class EBookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBookStoreApplication.class, args);
    }

}
