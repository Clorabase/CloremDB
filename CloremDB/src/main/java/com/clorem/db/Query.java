
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
    private final Node node;

    protected Query(Node node) {
        this.node = node;
    }

    /**
     * Sort children on the basis this condition
     * @param what The key like where claus in SQL
     * @param then The value to check against the key
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereGreater(String what, long then){
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path ('what') can only contain at most 1 parent node.",Reasons.REASONS_QUERY_ERROR);

        List<String> array = new ArrayList<>();
        for (String key : node.getChildren()){
            if (what.contains("/")){
                String parentKey = what.split("/")[0];
                String childKey = what.split("/")[1];
                if (node.node(key + "/" + parentKey).getNumber(childKey,then) > then)
                    array.add(key);
            } else {
                if (node.node(key).getNumber(what,then) > then)
                    array.add(key);
            }
        }
        return array;
    }

    /**
     * Sort children on the basis this condition
     * @param what The key like where claus in SQL
     * @param then The value to check against the key
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereSmaller(String what, long then){
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path can only contain at most 1 parent node.",Reasons.REASONS_QUERY_ERROR);

        List<String> array = new ArrayList<>();
        for (String key : node.getChildren()){
            if (what.contains("/")){
                String parentKey = what.split("/")[0];
                String childKey = what.split("/")[1];
                if (node.node(key + "/" + parentKey).getNumber(childKey,then) < then)
                    array.add(key);
            } else {
                if (node.node(key).getNumber(what,then) < then)
                    array.add(key);
            }
        }
        return array;
    }

    /**
     * Sort children on the basis this condition
     * @param what The key like where claus in SQL
     * @param to The value to check against the key
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereEqual(String what, long to){
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path ('what') can only contain at most 1 parent node.",Reasons.REASONS_QUERY_ERROR);

        List<String> array = new ArrayList<>();
        for (String key : node.getChildren()){
            if (what.contains("/")){
                String parentKey = what.split("/")[0];
                String childKey = what.split("/")[1];
                if (node.node(key + "/" + parentKey).getNumber(childKey,to) == to)
                    array.add(key);
            } else {
                if (node.node(key).getNumber(what,to) == to)
                    array.add(key);
            }
        }
        return array;
    }

    /**
     * Sort children on the basis this condition
     * @param what The key like where claus in SQL
     * @param to The value to check against the key
     * @param ignoreCase true to ignore case, false otherwise
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereEqual(String what, String to, boolean ignoreCase){
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path ('what') can only contain at most 1 parent node.",Reasons.REASONS_QUERY_ERROR);

        List<String> array = new ArrayList<>();
        for (String key : node.getChildren()) {
            if (what.contains("/")) {
                String parentKey = what.split("/")[0];
                String childKey = what.split("/")[1];
                String value = node.node(key + "/" + parentKey).getString(childKey, to);
                if (ignoreCase) {
                    if (value.equalsIgnoreCase(to))
                        array.add(key);
                    else if (value.equals(to))
                        array.add(key);
                }
            } else {
                String value = node.node(key).getString(what, to);
                if (ignoreCase) {
                    if (value.equalsIgnoreCase(to))
                        array.add(key);
                    else if (value.equals(to))
                        array.add(key);
                }
            }
        }
        return array;
    }

    /**
     * Sort children on the basis this condition
     * @param what The key like where claus in SQL
     * @param contains The value to check against the key
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereContains(String what, String contains){
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path ('what') can only contain at most 1 parent node.",Reasons.REASONS_QUERY_ERROR);

        List<String> array = new ArrayList<>();
        for (String key : node.getChildren()){
            if (what.contains("/")){
                String parentKey = what.split("/")[0];
                String childKey = what.split("/")[1];
                if (node.node(key + "/" + parentKey).getString(childKey,childKey).contains(contains))
                    array.add(key);
            } else {
                if (node.node(key).getString(what,contains).contains(contains))
                    array.add(key);
            }
        }
        return array;
    }


    /**
     * Sort children on the basis this condition
     * @param what The key like where claus in SQL
     * @param is The value to check against the key
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereBoolean(String what, boolean is){
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path ('what') can only contain at most 1 parent node.",Reasons.REASONS_QUERY_ERROR);

        List<String> array = new ArrayList<>();
        for (String key : node.getChildren()){
            if (what.contains("/")){
                String parentKey = what.split("/")[0];
                String childKey = what.split("/")[1];
                if (node.node(key + "/" + parentKey).getBoolean(childKey,is) == is)
                    array.add(key);
            } else {
                if (node.node(key).getBoolean(what,is) == is)
                    array.add(key);
            }
        }
        return array;
    }
}
