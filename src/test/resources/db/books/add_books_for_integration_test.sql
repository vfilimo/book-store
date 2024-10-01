INSERT INTO categories (id, name, is_deleted)
VALUES (1, 'Science', 0),
       (2, 'Horror', 0);


INSERT INTO books (id, title, author, isbn, price, is_deleted)
VALUES
(1, 'The Science of Everything', 'John Doe', '978-1234567890', 19.99, 0),
(2, 'Horror Stories', 'Jane Smith', '978-0987654321', 14.99, 0),
(3, 'Daughters of Darkness', 'Jane Smith', '978-6677889900', 12.99, 0);

INSERT INTO books_categories (book_id, category_id)
VALUES
(1, 1),
(2, 2),
(3, 2);
