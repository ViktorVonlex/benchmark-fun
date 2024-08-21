const express = require("express");
const { Pool } = require("pg");

const app = express();
const port = 8080;

// Set up PostgreSQL client
const pool = new Pool({
  user: "testuser",
  host: "localhost",
  database: "testdb",
  password: "testpassword",
  port: 5432,
});

// Helper function to get an author by ID
async function getAuthorById(id) {
  const res = await pool.query("SELECT * FROM authors WHERE id = $1", [id]);
  return res.rows[0]; // Assuming there's only one author per ID
}

// Helper function to get books by author ID
async function getBooksByAuthorId(authorId) {
  const res = await pool.query("SELECT * FROM books WHERE author_id = $1", [
    authorId,
  ]);
  return res.rows; // Returning all books for the author
}

// GET endpoint equivalent to the Quarkus one
app.get("/hello/getAuthor/:id", async (req, res) => {
  const authorId = req.params.id;

  try {
    // Execute both queries concurrently
    const [author, books] = await Promise.all([
      getAuthorById(authorId),
      getBooksByAuthorId(authorId),
    ]);

    if (author) {
      // Combine the results into a single response
      res.json({
        author: author,
        books: books,
      });
    } else {
      res.status(404).send("Author not found");
    }
  } catch (error) {
    console.error(error);
    res.status(500).send("An error occurred");
  }
});

app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});
