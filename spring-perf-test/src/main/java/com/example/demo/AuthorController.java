package com.example.demo;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public String hello() {
        return "No moc nečum!";
    }

    @GetMapping("/getAuthor/{id}")
    public ResponseEntity<Object> getAuthor(@PathVariable Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        List<Book> books = bookRepository.findByAuthorId(id);

        return ResponseEntity.ok()
            .body(Map.of("author", author, "books", books));
    }
}
