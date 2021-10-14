package com.clorem.db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This is the helper class for Clorem database. You can perform CRUD operation using this class.
 * This class is similar to that of realtime database. In other words, this database is local as well as online implementation of
 * firebase database.
 *
 * In CloremDB, data is stored as a JSON tree. If you know JSON its good, if not then refer this as the filesystem starting with
 * root directory. You can only access files which are in same directory in witch you are, Similarly, here you can only add,update
 * or remove child which are in your current node. See {@link <a href="API Reference & Guide">http://google.com</a>} for more information
 */
public class Database {

    private final JSONObject root;
    private JSONObject object;
    private final File databaseFile;
    private boolean inNewNode;
    private final ArrayList<JSONObject> parents;
    private final ArrayList<String> parentsNames;

    protected Database(File db) {
        try {
            FileInputStream in = new FileInputStream(db);
            byte[] bytes = new byte[(int) db.length()];
            in.read(bytes);
            in.close();
            String json = new String(bytes);
            object = new JSONObject(json);
        } catch (IOException | JSONException e) {
            object = new JSONObject();
        }
        parents = new ArrayList<>();
        parentsNames = new ArrayList<>();
        parents.add(object);
        parentsNames.add("root");
        this.root = object;
        databaseFile = db;
    }

    /**
     * Enters the node present in the current node.
     * @param name The name of the node.
     * @return The same database instance with the new node.
     * @throws CloremDatabaseException if node was not found in the current node.
     */
    public Database node(String name){
        try {
            if (inNewNode)
                throw new CloremDatabaseException("How can there be an existing node under a newly created one ?",Reasons.REASONS_NODE_NOT_FOUND);
            object = object.getJSONObject(name);
            parents.add(object);
            parentsNames.add(name);
        } catch (JSONException e) {
            throw new CloremDatabaseException(name + " is not present in current node",Reasons.REASONS_NODE_NOT_FOUND);
        }
        return this;
    }


    /**
     * Creates a new node under the current node.
     * @param name The name of the new node.
     * @return The same database instance with the newly created node.
     */
    public Database newNode(String name){
        try {
            object.put(name,new JSONObject());
            object = object.getJSONObject(name);
            parents.add(object);
            parentsNames.add(name);
            inNewNode = true;
            return this;
        } catch (JSONException e) {
            throw new CloremDatabaseException("Something horribly gone wrong !. This problem should only occur when the database files are edited manually !. Please create an issue on github",Reasons.REASONS_NODE_EXPECTED);
        }
    }


    /**
     * Creates a new key-value pair in the current node if not exist, otherwise this will update its value.
     * @param key The key to access the value
     * @param value The string value to store.
     * @return The same database instance.
     */
    public Database put(String key, String value){
        try {
            object.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github",Reasons.REASONS_UNKNOWN);
        }
        return this;
    }


    /**
     * Creates a new key-value pair in the current node if not exist, otherwise this will update its value.
     * @param key The key to access the value
     * @param value The long value to store.
     * @return The same database instance.
     */
    public Database put(String key, long value){
        try {
            object.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github",Reasons.REASONS_UNKNOWN);
        }
        return this;
    }


    /**
     * Creates a new key-value pair in the current node if not exist, otherwise this will update its value.
     * @param key The key to access the value
     * @param value The boolean value to store.
     * @return The same database instance.
     */
    public Database put(String key, boolean value){
        try {
            object.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github",Reasons.REASONS_UNKNOWN);
        }
        return this;
    }


    /**
     * Creates a new key-value pair in the current node if not exist, otherwise this will update its value.
     * @param key The key to access the value
     * @param value The double value to store.
     * @return The same database instance.
     */
    public Database put(String key, double value){
        try {
            object.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github",Reasons.REASONS_UNKNOWN);
        }
        return this;
    }


    /**
     * Creates a new key-value pair in the current node if not exist, otherwise this will update its value.
     * @param key The key to access the value
     * @param value The int value to store.
     * @return The same database instance.
     */
    public Database put(String key, int value){
        try {
            object.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github",Reasons.REASONS_UNKNOWN);
        }
        return this;
    }


    /**
     * Creates a new key-list pair in the current node if not exist, otherwise this will update its value.
     * @param key The key to access the list
     * @param list The list of type string to store.
     * @return The same database instance.
     */
    public Database put(String key, ArrayList<String> list){
        try {
            object.put(key,new JSONArray(list));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github",Reasons.REASONS_UNKNOWN);
        }
        return this;
    }


    /**
     * Creates a new key-list pair in the current node if not exist, otherwise this will update its value.
     * @param key The key to access the list
     * @param list The list of type int to store.
     * @return The same database instance.
     */
    public Database put(String key, List<Integer> list){
        try {
            object.put(key,new JSONArray(list));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github",Reasons.REASONS_UNKNOWN);
        }
        return this;
    }


    /**
     * Deletes the node or child of the current node.
     * @param node_or_child The name of node or child
     * @return Same database instance
     */
    public Database remove(String node_or_child){
        object.remove(node_or_child);
        return this;
    }


    /**
     * Get the string value from its key which is present in the current node.
     * @param key The key of the value
     * @return The value that the key holds
     * @throws CloremDatabaseException if key does not holds the the value of type 'String'
     */
    public String getString(String key){
        try {
            return object.getString(key);
        } catch (JSONException e){
            throw new CloremDatabaseException(key + " does not have the value of type 'String'",Reasons.REASONS_INVALID_TYPE);
        }
    }

    /**
     * Get the int value from its key which is present in the current node.
     * @param key The key of the value.
     * @return The value that the key holds.
     * @throws CloremDatabaseException if key does not holds the the value of type 'int'
     */
    public int getInt(String key){
        try {
            return object.getInt(key);
        } catch (JSONException e) {
            throw new CloremDatabaseException(key + "does not hold the value of type 'int'",Reasons.REASONS_INVALID_TYPE);
        }
    }

    /**
     * Get the boolean value from its key which is present in the current node.
     * @param key The key of the value.
     * @return The value that the key holds.
     * @throws CloremDatabaseException if key does not holds the the value of type 'boolean'
     */
    public boolean getBoolean(String key){
        try {
            return object.getBoolean(key);
        } catch (JSONException e){
            throw new CloremDatabaseException(key + " does not have the value of type 'String'",Reasons.REASONS_INVALID_TYPE);
        }
    }

    /**
     * Get the long value from its key which is present in the current node.
     * @param key The key of the value.
     * @return The value that the key holds.
     * @throws CloremDatabaseException if key does not holds the the value of type 'long'
     */
    public long getLong(String key){
        try {
            return object.getLong(key);
        } catch (JSONException e) {
            throw new CloremDatabaseException(key + "does not hold the value of type 'int'",Reasons.REASONS_INVALID_TYPE);
        }
    }

    /**
     * Get the double value from its key which is present in the current node.
     * @param key The key of the value.
     * @return The value that the key holds.
     * @throws CloremDatabaseException if key does not holds the the value of type 'double'
     */
    public double getDouble(String key){
        try {
            return object.getDouble(key);
        } catch (JSONException e) {
            throw new CloremDatabaseException(key + "does not hold the value of type 'int'",Reasons.REASONS_INVALID_TYPE);
        }
    }

    /**
     * Get the list from its key which is present in the current node.
     * @param key The key of the list.
     * @return The list that the key holds.
     * @throws CloremDatabaseException if key does not holds the the list of type 'String'
     */
    public List<String> getListString(String key){
        List<String> elements = new ArrayList<>();
        try {
            JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++) {
                elements.add((String) array.get(i));
            }
        } catch (JSONException e) {
            throw new CloremDatabaseException(key + "does not hold a list or a list of type 'String'",Reasons.REASONS_INVALID_TYPE);
        }
        return elements;
    }

    /**
     * Get the list from its key which is present in the current node.
     * @param key The key of the list.
     * @return The list that the key holds.
     * @throws CloremDatabaseException if key does not holds the the list of type 'int'
     */
    public List<Integer> getListInt(String key){
        List<Integer> elements = new ArrayList<>();
        try {
            JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++) {
                elements.add((int) array.get(i));
            }
        } catch (JSONException e) {
            throw new CloremDatabaseException(key + "does not hold a list or a list of type 'int'",Reasons.REASONS_INVALID_TYPE);
        }
        return elements;
    }

    /**
     * Get the values of all the children present in current node
     * @param type The type in which you want to retrieve values.
     * @return The list of the values.
     * @throws CloremDatabaseException if key does not holds the the list of type provided.
     */
    public <T> ArrayList<T> getChildrenValues(Class<T> type){
        ArrayList<T> children = new ArrayList<>();
        try {
            for (Iterator<String> it = object.keys(); it.hasNext(); ) {
                String key = it.next();
                children.add(type.cast(object.get(key)));
            }
        } catch (ClassCastException | JSONException e) {
            throw new CloremDatabaseException("This node do not contain all the values of type " + type.getName() + ". Use type Object instead",Reasons.REASONS_INVALID_TYPE);
        }
        return children;
    }

    /**
     * Get the keys of all the children present in current node
     * @return The list of the keys.
     */
    public ArrayList<String> getChildren(){
        ArrayList<String> children = new ArrayList<>();
        for (Iterator<String> it = object.keys(); it.hasNext(); ) {
            String child = it.next();
            children.add(child);
        }
        return children;
    }


    /**
     * Commit the changes to the database. This method will erase everything from the memory after writing it to database.
     * Also, the database will be started from root node.This should be the last call of this class as this method calls
     * garbage collector to free up memory. This method's runs synchronously.
     */
    public synchronized void commit() {
        try {
            PrintWriter writer = new PrintWriter(databaseFile);
            writer.write(root.toString());
            writer.close();
            root();
            System.gc();
        } catch (FileNotFoundException e) {
            throw new CloremDatabaseException("Database missing !, database files have been deleted or moved",Reasons.REASONS_DATABASE_CORRUPTED);
        }
    }

    /**
     * Goes to the root node of database.
     * @return The same database instance after moving to root.
     */
    public Database root(){
        object = root;
        return this;
    }

    /**
     * Goes one node back from the current node in the database.
     * @return The same database instance after moving back.
     */
    public Database back(){
        if (object != root){
            object = parents.get(parents.size() -2);
            parents.remove(parents.size() - 1);
        }
        return this;
    }

    /**
     * Use this to run queries on your database. All queries should be run from the grandparent
     * node of the child you want to query. Grandparent means the parent of the parent node of this child.
     * @return Query object to run queries.
     */
    public Query query(){
        return new Query(this);
    }

}

