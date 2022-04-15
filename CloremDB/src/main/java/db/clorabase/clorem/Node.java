package db.clorabase.clorem;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.java.json.JSONArray;
import org.java.json.JSONException;
import org.java.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class represent a node in the database. A node is like a folder which may contain other folders or files. You can perform all the CRUD
 * operations on a node using this class.
 * See {@link <a href="API Reference & Guide">https://github.com/ErrorxCode/CloremDB</a>} for more information
 */
public class Node {
    protected JSONObject root;
    protected JSONObject object;
    public String name;

    protected Node(Map<String, ?> map) {
        object = new JSONObject(map);
        root = object;
    }


    public Node(JSONObject object, JSONObject root, String name){
        this.object = object;
        this.root = root;
        this.name = name;
    }

    /**
     * Enters into the node present in the current node if exist, creates otherwise.
     * @param name The name or relative path of the node.
     * @return The new node.
     */
    public Node node(@NonNull String name){
        try {
            if (name.contains("/")){
                if (name.startsWith("/"))
                    name = name.substring(1);
                String[] nodes = name.split("/");
                JSONObject newObject = object;
                for (String node : nodes){
                    if (newObject.optJSONObject(node) == null){
                        JSONObject jsonObject = new JSONObject();
                        newObject.put(node,jsonObject);
                        newObject = jsonObject;
                    } else
                        newObject = newObject.optJSONObject(node);
                }
                return new Node(newObject,root,name);
            } else {
                JSONObject jsonObject = object.optJSONObject(name);
                if (jsonObject == null){
                    jsonObject = new JSONObject();
                    object.put(name,jsonObject);
                }
                return new Node(jsonObject,root,name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Cannot create node for some reason.",Reasons.REASONS_NODE_CREATION_ERROR);
        }
    }


    /**
     * Deletes the current node including its children. You don't need to call commit() after calling this.
     * Any operation performed on the node after calling this method will result in {@code NullPointerException}
     * as this node becomes null when deleted.
     */
    public void delete(@NonNull String name){
        object.remove(name);
        object = null;
        commit();
    }

    /**
     * Inserts a object to the database node. Object can be POJO or arraylist of type string or integer.
     * if the object is of type arraylist but is empty, you will get {@code NullPointerException} or if it is of type other than
     * supported type, you will get {@code CloremDatabaseException}.
     * @param key The key of the object.
     * @param object The object to be inserted.
     * @return This node.
     */
    public Node put(@NonNull String key,@NonNull Object object){
        try {
            if (object instanceof List list){
                Class<?> aClass = list.get(0).getClass();
                if (aClass == String.class)
                    this.object.put(key, new JSONArray(list));
                else if (aClass == Integer.class)
                    this.object.put(key, new JSONArray(list));
                else
                    throw new CloremDatabaseException("Cannot put list of objects of type " + aClass.getName() + " in the database.",Reasons.REASONS_INVALID_TYPE);
            } else
                this.object.put(key,new JSONObject(new Gson().toJson(object)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Creates a new key-value pair in the current node. Updates if already exist.
     * @param key The key to access the value
     * @param value The string value to put. null to remove existing mapping.
     * @return The current node
     */
    public Node put(@NonNull String key,@Nullable String value){
        try {
            object.put(key,value);
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github regarding this error\n\n---[Stack trace]---\n" + e.getLocalizedMessage(),Reasons.REASONS_UNKNOWN);
        }
    }


    /**
     * Creates a new key-value pair in the current node. Updates if already exist.
     * @param key The key to access the value
     * @param value The long or int value to put.
     * @return The current node
     */
    public Node put(@NonNull String key, long value){
        try {
            object.put(key,value);
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github regarding this error\n\n---[Stack trace]---\n" + e.getLocalizedMessage(),Reasons.REASONS_UNKNOWN);
        }
    }


    /**
     * Creates a new key-value pair in the current node. Updates if already exist.
     * @param key The key to access the value
     * @param value The boolean value to put.
     * @return The current node
     */
    public Node put(@NonNull String key, boolean value){
        try {
            object.put(key,value);
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github regarding this error\n\n---[Stack trace]---\n" + e.getLocalizedMessage(),Reasons.REASONS_UNKNOWN);
        }
    }


    /**
     * Creates a new key-value pair in the current node. Updates if already exist.
     * @param key The key to access the value
     * @param value The double value to put.
     * @return The current node
     */
    public Node put(@NonNull String key, double value){
        try {
            object.put(key,value);
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github regarding this error\n\n---[Stack trace]---\n" + e.getLocalizedMessage(),Reasons.REASONS_UNKNOWN);
        }
    }
    
    
    /**
     * Puts all the data from the map to the current node.
     * @param data The map to put.
     * @return The current node
     */
    @SuppressWarnings("NewApi")
    public Node put(@NonNull Map<String,Object> data){
        data.forEach((key,value) -> object.put(key,value));
        return this;
    }

    /**
     * Gets the string from the current node in the database.
     * @param key The key of the value
     * @return The value that the key holds, or defaultValue if key not exist.
     */
    public String getString(@NonNull String key,String defaultValue){
        return object.optString(key).isEmpty() ? defaultValue : object.optString(key);
    }

    /**
     * Gets the number from the database. This number could either be an int or a long depending on what did you put.
     * @param key The key of the value
     * @return The value that the key holds, or defaultValue if key not exist.
     */
    public long getNumber(@NonNull String key,long defaultValue){
        return object.optInt(key) == 0 ? defaultValue : object.optInt(key);
    }

    /**
     * Gets the boolean from the current node in the database.
     * @param key The key of the value
     * @return The value that the key holds, or defaultValue if key not exist.
     */
    public boolean getBoolean(@NonNull String key,boolean defaultValue){
        try {
            return object.getBoolean(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }


    /**
     * Gets the floating point number from the current node in the database. This number can either be a float or a double depending on what did you put.
     * @param key The key of the value.
     * @return The value that the key holds, or defaultValue if key not exist.
     */
    public double getDecimal(@NonNull String key,double defaultValue){
        return Double.isNaN(object.optDouble(key)) ? defaultValue : object.optDouble(key);
    }


    /**
     * Gets the POJO associated with the key.
     * @param key The key of the POJO.
     * @param clazz type of the object that the key contain.
     * @return The Object of class type 'clazz'
     */
    public <T> T getObject(@NonNull String key,Class<T> clazz){
        JSONObject json = object.optJSONObject(key);
        if (json == null)
            return null;
        else
            return new Gson().fromJson(json.toString(),clazz);
    }


    /**
     * Gets the List of type String from the current node in the database.
     * @param key The key of the list.
     * @return The list that the key holds, otherwise an empty list but never null.
     */
    @NonNull
    public List<String> getListString(@NonNull String key){
        List<String> elements = new ArrayList<>();
        try {
            JSONArray array = object.optJSONArray(key);
            if (array == null)
                return new ArrayList<>();
            else {
                for (int i = 0; i < array.length(); i++) {
                    elements.add(array.getString(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException(key + " does not hold a list or of type 'String'",Reasons.REASONS_INVALID_TYPE);
        }
        return elements;
    }

    /**
     * Gets the List of type Integer from the current node in the database.
     * @param key The key of the list.
     * @return The list that the key holds, otherwise an empty list but never null.
     */
    @NonNull
    public List<Integer> getListInt(String key){
        List<Integer> elements = new ArrayList<>();
        try {
            JSONArray array = object.optJSONArray(key);
            if (array == null)
                return new ArrayList<>();
            else {
                for (int i = 0; i < array.length(); i++) {
                    elements.add(array.getInt(i));
                }
            }
        } catch (JSONException e) {
            throw new CloremDatabaseException(key + "does not hold a list or a list of type 'int'",Reasons.REASONS_INVALID_TYPE);
        }
        return elements;
    }

    /**
     * Gets all the data from the current node in the form of map. This map will not contain any nested node.
     * @return The map as data.
     */
    public Map<String,Object> getData(){
        Map<String,Object> map = new HashMap<>();
        for (String key : getChildren()){
            Object value = object.get(key);
            if (!(value instanceof JSONObject))
                map.put(key,object.get(key));
        }
        return map;
    }

    /**
     * Get the keys of all the children present in this node
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
     * Use this to run queries on your database. All queries should be run from the parent
     * node which contains many same types of sub-nodes of the child you want to query.
     * For example if you have a node 'users' with many sub-nodes as "userA","userB',"userC"
     * then you have to query from "users" node.
     * @return Query object to run queries.
     */
    public Query query(){
        return new Query(this);
    }

    /**
     * Commit the changes to the database. This method will erase everything from the memory after writing it to database.
     * This should be the last call of database as this will call garbage collector to free up memory which will destroy the current instance of database.
     * This method's runs synchronously.
     */
    public void commit() {
        Clorem.commit(root);
    }
}

