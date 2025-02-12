import db.clorabase.clorem.CloremObject;

public class Book implements CloremObject {
    private int id;
    private String title;
    private String isbn;
    private int authorId;
    private int publisherId;

    // Constructor
    public Book(int id, String title, String isbn, int authorId, int publisherId) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.authorId = authorId;
        this.publisherId = publisherId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public int getPublisherId() { return publisherId; }
    public void setPublisherId(int publisherId) { this.publisherId = publisherId; }

    // toString method
    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', isbn='" + isbn + "', authorId=" + authorId + ", publisherId=" + publisherId + "}";
    }

    @Override
    public String getKey() {
        return String.valueOf(id);
    }

    @Override
    public String getVolume() {
        return "Library.Books";
    }
}
