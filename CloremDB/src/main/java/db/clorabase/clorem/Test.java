package db.clorabase.clorem;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Test {
    public static void main(String[] args) {
        var db = Clorem.getInstance(new File("C:\\Users\\rahil\\Desktop\\est"));
//        db.put(new User("xyz","rahil","123"));
//        db.put(new User("abc","rahil2","123"));
//        db.put(new User("key","nandan","any"));
//        db.put(new User("x0x","tabish","asdf"));
    }
}


class User implements CloremObject{
    public String name;
    public String password;
    public String id;

    public User(String id,String name, String password) {
        this.name = name;
        this.password = password;
        this.id = id;
    }

    @Override
    public String getKey() {
        return id;
    }

    @Override
    public String getVolume() {
        return "demoVolume";
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
