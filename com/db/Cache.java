package db;


import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class Cache<T> {
    private final int maxSize;
    private final LinkedHashMap<String, T> cache;
    private final Function<T, String> keyFactory;

    public Cache(int maxSize, Function<T, String> keyFactory) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true);
        this.keyFactory = keyFactory;
    }

    public void cache(T element) {
        String key = keyFactory.apply(element);
        if (cache.containsKey(key)) {
            cache.get(key);
        } else {
            if (cache.size() >= maxSize) {
                String keyToRemove = cache.keySet().iterator().next();
                cache.remove(keyToRemove);
            }
            cache.put(key, element);
        }
    }

    public T get(String key) {
        return cache.get(key);
    }
    public Stream<T> get() {
        return cache.values().stream();
    }
    public boolean inCache(String check) {
        return cache.containsKey(check);
    }

    public void remove(String key) {
        cache.remove(key);
    }
    public void remove(T element) {
        cache.remove(keyFactory.apply(element));
    }
    public void forEach(BiConsumer<String, T> biConsumer) {
        cache.forEach(biConsumer);
    }
}