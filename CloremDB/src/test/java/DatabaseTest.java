import java.io.File;

import db.clorabase.clorem.Clorem;
import db.clorabase.clorem.Volume;

public class DatabaseTest {
    public static void main(String[] args) {
        // --------------- Creating the volume ---------------
        Clorem db = Clorem.getInstance(new File("C:\\Users\\Infrared x\\Desktop\\testDB"));
        Volume b1 = db.volume("Library").volume("Books");

        // --------------- Inserting the objects ---------------

        db.insert(new Book(1,"Test book","1234567890",1,1));
        b1.insert(new Book(2,"Test book 2","1234567891",2,2));
        b1.insert(new Book(3,"Test book 3","1234567892",3,3));
        b1.insert(new Book(4,"Test book 4","1234567893",4,4));

        // --------------- Reading the objects ---------------

        System.out.println(b1.fetch("1"));
        System.out.println(b1.fetch("2"));
        System.out.println(b1.fetch("3"));
        System.out.println(b1.fetch("4"));

        // --------------- Querying the objects ---------------

        b1.query(Book.class).whereEqual("title","Test book 3").forEach(System.out::println);
        b1.query(Book.class).whereNumerical("id",2,'>').forEach(System.out::println);

        // --------------- Updating the objects ---------------
        b1.update("2",Book.class,book -> book.setTitle("Test book 2 updated"));
        b1.update("2",Book.class,book -> book.setTitle("Test book 2 updated"));

        // --------------- Deleting the objects and volume ---------------
        b1.delete("3");
        b1.delete("4");
        b1.deleteVolume();
    }
}
