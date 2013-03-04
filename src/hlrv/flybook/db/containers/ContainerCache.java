package hlrv.flybook.db.containers;

import java.util.LinkedList;
import java.util.TreeMap;

import com.vaadin.data.Container;

/**
 * Class that could be used to cache Containers.
 * 
 * NOTE: This is presently not used in code! It seems to be unstable practice to
 * share containers with different components as container data sources.
 * 
 * @param <K>
 *            Keytype
 * @param <C>
 *            ContainerType
 */
public class ContainerCache<K, C extends Container> {

    private TreeMap<K, C> cache = new TreeMap<K, C>();
    private LinkedList<K> insertionOrder = new LinkedList<K>();
    private int size = 50;

    public ContainerCache() {
    }

    public ContainerCache(int size) {
        this.size = size;
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public C get(K key) {
        return cache.get(key);
    }

    public void put(K key, C container) {
        if (cache.size() > size) {
            cache.remove(insertionOrder.removeFirst());
        }
        cache.put(key, container);
        insertionOrder.addLast(key);
    }

    public void remove(K key) {
        cache.remove(key);
        insertionOrder.remove(key);
    }

}
