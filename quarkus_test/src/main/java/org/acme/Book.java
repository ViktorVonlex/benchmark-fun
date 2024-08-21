package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@RegisterForReflection
@Entity
@AllArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    private Long id;

    private String title;

    private Long authorId;

    public static Uni<Book> findById(Long id, PgPool client) {
        return client
            .preparedQuery("SELECT * FROM books WHERE id = $1")
            .execute(Tuple.of(id))
            .onItem()
            .transform(RowSet::iterator)
            .onItem()
            .transform(iterator -> iterator.hasNext() ? iterator.next() : null)
            .onItem()
            .transform(Book::from);
    }

    public static Uni<List<Book>> findByAuthorId(Long authorId, PgPool client) {
        return client
            .preparedQuery("SELECT * FROM books WHERE author_id = $1")
            .execute(Tuple.of(authorId))
            .onItem()
            .transform(rowSet -> {
                List<Book> books = new ArrayList<>();
                for (Row row : rowSet) {
                    books.add(Book.from(row));
                }
                return books;
            });
    }

    private static Book from(Row row) {
        return new Book(
            row.getLong("id"),
            row.getString("title"),
            row.getLong("author_id")
        );
    }
}
