package org.acme;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/hello")
public class GreetingResource {

    private final PgPool client;

    public GreetingResource(PgPool client) {
        this.client = client;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "No moc nečum!";
    }

    @POST
    @Path("/addPerson")
    public Response addPerson(Person person) {
        return Response.ok(person).build();
    }

    @GET
    @Path("/getAuthor/{id}")
    public Uni<Response> getAuthor(Long id) {
        Uni<Author> author = Author.findById(id, client);
        Uni<List<Book>> books = Book.findByAuthorId(id, client);

        return Uni.combine()
            .all()
            .unis(author, books)
            .asTuple()
            .onItem()
            .transform(tuple -> {
                Author a = tuple.getItem1();
                List<Book> b = tuple.getItem2();
                Map<String, Object> result = new HashMap<>();
                result.put("author", a);
                result.put("books", b);
                return Response.ok(result).build();
            });
    }
}
