
package com.clorem.db;

import java.util.ArrayList;
import java.util.List;

/**
 * This class help you to filter data from the database. You can retrieve data through verier conditions.
 * Every method of this class must be called from the grandparent node of the child you want to query.
 * Grandparent means the parent of the parent node of this child.
 *
 * Consider the following example :-  ( ctrl + click the class name to see actual comment)
 *
 * Node users       (Grandparent node)
 *      Node user1      (Parent node)
 *          name : Mr x    (child)
 *      Node user2
 *          name : Mr coder
 *      Node user3
 *          name : Mr programmer
 *
 *  So the query method must be called from 'users' node, like
 *  {@code getDatabase().node("users").query().whereContains("name","Mr");}
 *
 * This will return us list like this [user1,user2,user3]
 */
public class Query {

    private final Database database;

    protected Query(Database database) {
        this.database = database;
    }

    /**
     * Sort children where the value of the key 'what' is grater then 'then'.
     * @param what The key as column name for the where claus in SQL
     * @param then the number which should be grater then the value.
     * @return list of the sorted keys.
     */
    public List<String> whereGreater(String what, long then){
        List<String> array = new ArrayList<>();
        for (String node : database.getChildren()){
            database.node(node);
            try {
                if (database.getInt(what) > then)
                    array.add(node);
            } catch (CloremDatabaseException ignored) {}
            database.back();
        }
        return array;
    }

    /**
     * Sort children where the value of the key 'what' is smaller then 'then'.
     * @param what The key as column name for the where claus in SQL
     * @param then the number which should be smaller then the value.
     * @return list of the sorted keys.
     */
    public List<String> whereSmaller(String what, long then){
        List<String> array = new ArrayList<>();
        for (String node : database.getChildren()){
            database.node(node);
            try {
                if (database.getInt(what) < then)
                    array.add(node);
            } catch (CloremDatabaseException ignored) {}
            database.back();
        }
        return array;
    }

    /**
     * Sort children where the value of the key 'what' is equal to 'to'.
     * @param what The key as column name for the where claus in SQL
     * @param to the number to check equality of the value.
     * @return list of the sorted keys.
     */
    public List<String> whereEqual(String what, long to){
        List<String> array = new ArrayList<>();
        for (String node : database.getChildren()){
            database.node(node);
            try {
                if (database.getInt(what) == to)
                    array.add(node);
            } catch (CloremDatabaseException ignored) {}
            database.back();
        }
        return array;
    }

    /**
     * Sort children where the value of the key 'what' is grater then 'then'.
     * @param what The key as column name for the where claus in SQL
     * @param to the string to check equality of the value.
     * @param ignoreCase if it has to checked ignoringCases
     * @return list of the sorted keys.
     */
    public List<String> whereEqual(String what, String to, boolean ignoreCase){
        List<String> array = new ArrayList<>();
        for (String node : database.getChildren()){
            database.node(node);
            try {
                if (ignoreCase)
                    if (database.getString(what).equalsIgnoreCase(to))
                        array.add(node);
                    else
                        if (database.getString(what).equals(to))
                            array.add(node);

            } catch (CloremDatabaseException ignored) {}
            database.back();
        }
        return array;
    }

    /**
     * Sort children where the value of the key 'what' contains 'contains'.
     * @param what The key as column name for the where claus in SQL
     * @param contains the string that should contain in the value.
     * @return list of the sorted keys.
     */
    public List<String> whereContains(String what, String contains){
        List<String> array = new ArrayList<>();
        for (String node : database.getChildren()){
            database.node(node);
            try {
                if (database.getString(what).contains(contains))
                    array.add(node);
            } catch (CloremDatabaseException ignored) {}
            database.back();
        }
        return array;
    }

    public List<String> whereBoolean(String where, boolean is){
        List<String> array = new ArrayList<>();
        for (String node : database.getChildren()){
            database.node(node);
            try {
                if (database.getBoolean(where) == is)
                    array.add(node);
            } catch (CloremDatabaseException ignored) {}
            database.back();
        }
        return array;
    }

    public List<String> whereGreaterOrEqual(String what, long then_or_to){
        return whereGreater(what,then_or_to - 1);
    }


    public List<String> whereSmallerOrEqual(String what, long then_or_to){
        return whereSmaller(what,then_or_to + 1);
    }
}
