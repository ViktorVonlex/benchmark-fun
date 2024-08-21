package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")
@RegisterForReflection
public class Author {

    @Id
    private Long id;

    private String name;

    public static Uni<Author> findById(Long id, PgPool client) {
        return client
            .preparedQuery("SELECT * FROM authors WHERE id = $1")
            .execute(Tuple.of(id))
            .onItem()
            .transform(RowSet::iterator)
            .onItem()
            .transform(iterator -> iterator.hasNext() ? iterator.next() : null)
            .onItem()
            .transform(Author::from);
    }

    private static Author from(Row row) {
        return new Author(row.getLong("id"), row.getString("name"));
    }
}
