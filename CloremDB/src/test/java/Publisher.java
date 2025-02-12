import db.clorabase.clorem.CloremObject;

public class Publisher implements CloremObject {
    private int id;
    private String name;
    private String address;

    // Constructor
    public Publisher(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // toString method
    @Override
    public String toString() {
        return "Publisher{id=" + id + ", name='" + name + "', address='" + address + "'}";
    }

    @Override
    public String getKey() {
        return String.valueOf(id);
    }

    @Override
    public String getVolume() {
        return "Library.Publishers";
    }
}
