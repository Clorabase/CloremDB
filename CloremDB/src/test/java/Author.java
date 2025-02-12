import db.clorabase.clorem.CloremObject;

public class Author implements CloremObject {
    private int id;
    private String name;
    private String nationality;

    // Constructor
    public Author(int id, String name, String nationality) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    // toString method
    @Override
    public String toString() {
        return "Author{id=" + id + ", name='" + name + "', nationality='" + nationality + "'}";
    }

    @Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getVolume() {
        return "Library.Authors";
    }
}
