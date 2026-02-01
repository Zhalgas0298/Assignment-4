Drop table if exists books;
Drop table if exists authors;

Create table authors (
    author_id Serial primary key,
    full_name Varchar(100) not null unique,
    country Varchar(80)
);

Create table books (
    book_id Serial primary key,
    title Varchar(200) not null,
    publish_year int not null,
    price numeric(10,2) not null check (price > 0),
    book_type varchar(20) not null check (book_type in ('EBOOK', 'PRINTED')),
    author_id int not null,
    foreign key (author_id) references authors(author_id),
    file_size_mb numeric(10,2),
    download_url text,
    pages int,
    shelf_code varchar(30),
    unique (title, author_id)
);

Insert into authors (full_name, country)
Values
    ('George R.R Martin', 'USA'),
    ('Stephen King', 'USA');

Insert into books (title, publish_year, price, book_type, author_id, file_size_mb, download_url)
Values
    ('A Game of Thrones', 1996, 3000, 'EBOOK', 1, 251.32, 'link');

Insert into books(title, publish_year, price, book_type, author_id, pages, shelf_code)
Values
    ('The Green Mile', 1996, 4590, 'PRINTED', 2, 359, A-5);