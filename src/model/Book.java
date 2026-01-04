package model;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookId;
    private String title;
    private String author;
    private Category category;
    private BookStatus status;

    public Book(int bookId, String title, String author, Category category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = BookStatus.AVAILABLE; // Default
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Category getCategory() {
        return category;
    }

    public BookStatus getStatus() {
        return status;
    }

    // Setters
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return title + " (ID: " + bookId + ") [" + status + "]";
    }
}
