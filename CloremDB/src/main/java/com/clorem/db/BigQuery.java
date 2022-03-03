package com.clorem.db;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;


/**
 * A BigQuery is yet another type of query which is used for complex and big data structure.
 * Given a node, BigQuery is capable to find any node in the database no matter how deep is it.
 * Nodes returned by this query are immutable and cannot be modified. Only use them for reading data.
 * However, if there is nested similar nodes (Nodes which have an extra parent), use {@link Query} class instead.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class BigQuery {
    protected final JSONObject root;
    protected final List<Map<String,Object>> maps = new ArrayList<>();
    protected final List<String> names = new ArrayList<>();

    /**
     * Construct a query which search for data from the given nodes onwards.
     * @param node The node from where to start querying.
     */
    public BigQuery(Node node){
        root = node.root;
        mapify(root,"root");
    }


    /**
     * Query and collect nodes which meet the given condition.
     * @param condition A boolean function which is evaluated as a condition.
     * @return Array of {@link Node} as results
     * @throws NullPointerException if the data is not same in all the nodes (under the querying node)
     * and the key in predicate is not present. To avoid this, use {@link Map#getOrDefault(Object, Object) method}
     */
    public Node[] where(@NonNull Predicate<Map<String,? super Object>> condition){
        Object[] nodes = maps.stream().filter(condition).map(map -> new Node(new JSONObject(map),root,names.get(maps.indexOf(map)))).toArray();
        return Arrays.copyOf(nodes,nodes.length,Node[].class);
    }

    /**
     * Returns nodes in which 'key' is = to 'value'
     * @param key The key of the data
     * @param value the value to be checked across
     * @return Array of {@link Node[]} containing matching results
     */
    public Node[] whereEqual(@NonNull String key,@NonNull Object value){
        return where(map -> Objects.equals(map.get(key), value));
    }


    /**
     * Returns nodes in which 'key' is > then 'value'
     * @param key The key of the data
     * @param value the value to be checked across
     * @return Array of {@link Node[]} containing matching results
     */
    public Node[] whereGreater(@NonNull String key,@NonNull Number value){
        if (value instanceof Long val)
            return where(stringMap -> ((long) stringMap.getOrDefault(key,0)) > val);
        else if (value instanceof Integer val)
            return where(stringMap -> ((int) stringMap.getOrDefault(key, 0)) > val);
        else if (value instanceof Double val)
            return where(stringMap -> ((double) stringMap.getOrDefault(key,0)) > val);
        else
            throw new CloremDatabaseException("Invalid number. the field does not denotes a number",Reasons.REASONS_INVALID_TYPE);
    }

    /**
     * Returns nodes in which 'key' is < then 'value'
     * @param key The key of the data
     * @param value the value to be checked across
     * @return Array of {@link Node[]} containing matching results
     */
    public Node[] whereSmaller(@NonNull String key,@NonNull Number value){
        if (value instanceof Long val)
            return where(stringMap -> ((long) stringMap.getOrDefault(key,0)) < val);
        else if (value instanceof Integer val)
            return where(stringMap -> ((int) stringMap.getOrDefault(key, 0)) < val);
        else if (value instanceof Double val)
            return where(stringMap -> ((double) stringMap.getOrDefault(key,0)) < val);
        else
            throw new CloremDatabaseException("Invalid number. the field does not denotes a number",Reasons.REASONS_INVALID_TYPE);
    }


    private void mapify(JSONObject object,String name){
        Iterator<String> iterator = object.keys();
        Map<String,Object> map = new HashMap<>();
        while (iterator.hasNext()){
            String key = iterator.next();
            if (object.opt(key) instanceof JSONObject obj){
                mapify(obj,key);
            } else
                map.put(key,object.opt(key));
        }
        names.add(name);
        maps.add(map);
    }
}
