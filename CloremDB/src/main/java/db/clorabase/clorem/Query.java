package db.clorabase.clorem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Query<T> {
    private final File directory;
    private final Class<T> type;

    public Query(File directory, Class<T> type) {
        this.directory = directory;
        this.type = type;
    }

    public List<T> filter(Predicate<T> predicate) {
        var files = directory.listFiles(File::isFile);
        if (files == null)
            return new ArrayList<>();

        var list = new ArrayList<T>();
        for (File file : files) {
            var obj = CloremUtils.readObject(file);
            if (type.isInstance(obj) && predicate.test(type.cast(obj))) {
                list.add(type.cast(obj));
            }
        }
        return list;
    }

    public List<T> whereEqual(String key, Object value) {
        var files = directory.listFiles(File::isFile);
        if (files == null)
            return new ArrayList<>();

        var list = new ArrayList<T>();
        for (File file : files) {
            var obj = CloremUtils.readObject(file);
            try {
                if (type.isInstance(obj)) {
                    var field = type.cast(obj).getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    if (field.get(obj).equals(value)) {
                        list.add(type.cast(obj));
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                continue;
            }
        }
        return list;
    }

    public List<T> whereNumerical(String key, Number value, char condition) {
        var files = directory.listFiles(File::isFile);
        if (files == null)
            return new ArrayList<>();

        var list = new ArrayList<T>();
        for (File file : files) {
            var obj = CloremUtils.readObject(file);
            try {
                if (type.isInstance(obj)) {
                    var field = type.cast(obj).getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    var fieldValue = (Number) field.get(obj);
                    switch (condition) {
                        case '<':
                            if (fieldValue.doubleValue() < value.doubleValue()) {
                                list.add(type.cast(obj));
                            }
                            break;

                        case '>':
                            if (fieldValue.doubleValue() > value.doubleValue()) {
                                list.add(type.cast(obj));
                            }
                            break;

                        case '=':
                            if (fieldValue.doubleValue() == value.doubleValue()) {
                                list.add(type.cast(obj));
                            }
                            break;

                        default:
                            throw new IllegalArgumentException("Invalid condition");
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }
}
