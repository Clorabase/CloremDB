
package db.clorabase.clorem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class help you to filter data from the database. You can retrieve data through verier conditions.
 * Every method of this class must be called from the grandparent node of the child you want to query.
 * Grandparent means the parent of the parent node of this child.
 * <p>
 * Consider the following example :-  ( ctrl + click the class name to see actual comment)
 * <p>
 * Node users       (Grandparent node)
 * Node user1      (Parent node)
 * name : Mr x    (child)
 * Node user2
 * name : Mr coder
 * Node user3
 * name : Mr programmer
 * <p>
 * So the query method must be called from 'users' node, like
 * {@code getDatabase().node("users").query().whereContains("name","Mr");}
 * <p>
 * This will return us list like this [user1,user2,user3]
 */
public class Query {
    private final Node node;

    protected Query(Node node) {
        this.node = node;
    }

    /**
     * Sort children on the basis this condition
     *
     * @param what The key like where claus in SQL
     * @param then The value to check against the key
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereGreater(String what, long then) {
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path ('what') can only contain at most 1 parent node.", Reasons.REASONS_QUERY_ERROR);

        List<String> array = new ArrayList<>();
        for (String key : node.getChildren()) {
            if (what.contains("/")) {
                String parentKey = what.split("/")[0];
                String childKey = what.split("/")[1];
                if (node.node(key + "/" + parentKey).getNumber(childKey, 0) > then)
                    array.add(key);
            } else {
                if (node.node(key).getNumber(what, then) > then)
                    array.add(key);
            }
        }
        return array;
    }

    /**
     * Sort children on the basis this condition
     *
     * @param what The key like where claus in SQL
     * @param then The value to check against the key
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereSmaller(String what, long then) {
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path can only contain at most 1 parent node.", Reasons.REASONS_QUERY_ERROR);

        List<String> array = new ArrayList<>();
        for (String key : node.getChildren()) {
            if (what.contains("/")) {
                String parentKey = what.split("/")[0];
                String childKey = what.split("/")[1];
                if (node.node(key + "/" + parentKey).getNumber(childKey,5) < then)
                    array.add(key);
            } else {
                if (node.node(key).getNumber(what, then) < then)
                    array.add(key);
            }
        }
        return array;
    }

    /**
     * Sort children on the basis this condition
     *
     * @param what The key like where claus in SQL
     * @param to   The value to check against the key
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereEqual(String what, long to) {
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path ('what') can only contain at most 1 parent node.", Reasons.REASONS_QUERY_ERROR);

        List<String> array = new ArrayList<>();
        for (String key : node.getChildren()) {
            if (what.contains("/")) {
                String parentKey = what.split("/")[0];
                String childKey = what.split("/")[1];
                long number = node.node(key + "/" + parentKey).getNumber(childKey,-786);
                if (number == to){
                    array.add(key);
                }
            } else {
                if (node.node(key).getNumber(what, to) == to)
                    array.add(key);
            }
        }
        return array;
    }

    /**
     * Sort children on the basis this condition
     *
     * @param what       The key like where claus in SQL
     * @param to         The value to check against the key
     * @param ignoreCase true to ignore case, false otherwise
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereEqual(String what, String to, boolean ignoreCase) {
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path ('what') can only contain at most 1 parent node.", Reasons.REASONS_QUERY_ERROR);

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
                } else {
                    if (value.equals(to))
                        array.add(key);
                }
            }
        }
        return array;
    }

    /**
     * Sort children on the basis of this condition. This is case-sensative
     *
     * @param what     The key like where claus in SQL
     * @param contains The value to check against the key
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereContains(String what, String contains) {
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path ('what') can only contain at most 1 parent node.", Reasons.REASONS_QUERY_ERROR);

        List<String> array = new ArrayList<>();
        for (String key : node.getChildren()) {
            if (what.contains("/")) {
                String parentKey = what.split("/")[0];
                String childKey = what.split("/")[1];
                String string = node.node(key + "/" + parentKey).getString(childKey, childKey);
                if (string.toLowerCase().contains(contains.toLowerCase()))
                    array.add(key);
            } else {
                String string = node.getString(key, what);
                if (string.toLowerCase().contains(contains.toLowerCase()))
                    array.add(key);
            }
        }
        return array;
    }


    /**
     * Sort children on the basis this condition
     *
     * @param what The key like where claus in SQL
     * @param is   The value to check against the key
     * @return list of parent node (keys) which satisfy the condition.
     */
    public List<String> whereBoolean(String what, boolean is) {
        if (what.startsWith("/"))
            what = what.substring(1);
        if (what.split("/").length > 2)
            throw new CloremDatabaseException("Child path ('what') can only contain at most 1 parent node.", Reasons.REASONS_QUERY_ERROR);

        List<String> array = new ArrayList<>();
        for (String key : node.getChildren()) {
            if (what.contains("/")) {
                String parentKey = what.split("/")[0];
                String childKey = what.split("/")[1];
                if (node.node(key + "/" + parentKey).getBoolean(childKey, is) == is)
                    array.add(key);
            } else {
                if (node.node(key).getBoolean(what, is) == is)
                    array.add(key);
            }
        }
        return array;
    }


    @SuppressWarnings("NewApi")
    public List<Node> fromQuery(String query) {
        var tokens = query.split(" ");
        var key = tokens[1];
        var operator = tokens[2];
        var value = tokens[3];
        int limit = 0;
        boolean isNumeric = true;
        if (tokens.length > 4)
            limit = Integer.parseInt(tokens[5]);
        List<String> results;
        switch (operator) {
            case "=":
                if (value.startsWith("'") && value.endsWith("'")) {
                    value = value.replaceAll("'", "");
                    results = whereEqual(key, value, false);
                    isNumeric = false;
                } else {
                    try {
                        results = whereEqual(key, Long.parseLong(value));
                    } catch (NumberFormatException e) {
                        isNumeric = false;
                        results = whereBoolean(key, Boolean.parseBoolean(value));
                    }
                }
                break;
            case "<":
                results = whereSmaller(key, Long.parseLong(value));
                break;
            case ">":
                results = whereGreater(key, Long.parseLong(value));
                break;
            case "contains":
                if (value.startsWith("'") && value.endsWith("'")) {
                    value = value.replaceAll("'", "");
                }
                results = whereContains(key, value);
                break;
            default:
                throw new CloremDatabaseException("Invalid query. Please refer to query documentation at https://github.com/ErrorxCode/CloremDB/wiki/Guide#quering-database", Reasons.REASONS_QUERY_ERROR);
        }
        boolean finalIsNumeric = isNumeric;
        return results.stream()
                .map(node::node)
                .limit(limit == 0 ? results.size() : limit)
                .sorted((o1, o2) -> {
                    if (finalIsNumeric)
                        return (int) (o1.getNumber(key, 0) - o2.getNumber(key, 0));
                    else
                        return o1.getString(key, "").compareToIgnoreCase(o2.getString(key, ""));
                })
                .toList();
    }
}
