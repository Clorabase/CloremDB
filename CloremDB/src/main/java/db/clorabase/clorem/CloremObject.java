package db.clorabase.clorem;

import java.io.Serializable;

/**
 * This interface is implemented by all the objects which are to be saved in the database.
 * The objects must implement this interface to be saved in the database.
 *
 * @author author Rahil khan
 * @version 1.0
 * @since 1.0
 */
public interface CloremObject extends Serializable {

    String getKey();
    String getVolume();
}
