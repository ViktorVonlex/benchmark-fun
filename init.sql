-- Create a sequence for authors' id
CREATE SEQUENCE authors_id_seq;

-- Create authors table with id using the sequence
CREATE TABLE authors (
    id INTEGER PRIMARY KEY DEFAULT nextval('authors_id_seq'),
    name VARCHAR(100) NOT NULL
);

-- Create a sequence for books' id
CREATE SEQUENCE books_id_seq;

-- Create books table with id using the sequence and a foreign key to authors
CREATE TABLE books (
    id INTEGER PRIMARY KEY DEFAULT nextval('books_id_seq'),
    title VARCHAR(200) NOT NULL,
    author_id INTEGER REFERENCES authors(id)
);

-- Insert some initial data into authors table
INSERT INTO authors (name) VALUES ('J.K. Rowling');
INSERT INTO authors (name) VALUES ('George R.R. Martin');
INSERT INTO authors (name) VALUES ('J.R.R. Tolkien');

-- Insert some initial data into books table
INSERT INTO books (title, author_id) VALUES ('Harry Potter and the Philosopher''s Stone', 1);
INSERT INTO books (title, author_id) VALUES ('A Game of Thrones', 2);
INSERT INTO books (title, author_id) VALUES ('The Hobbit', 3);

