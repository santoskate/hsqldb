package org.hsqldb.i18n;

import java.util.*;

/**
 * 
 * Internationalization Cache
 * 
 * @author CatarinaSantos
 * @version 0.1
 *
 */
public class I18nCache {
	private static I18nCache instance;
    private static Object monitor = new Object();
    private Map<String, Object> cache = Collections.synchronizedMap(new HashMap<String, Object>());

    private I18nCache() {
    }

    public void put(String cacheKey, Object value) {
        cache.put(cacheKey, value);
    }

    public Object get(String cacheKey) {
        return cache.get(cacheKey);
    }

    public void clear(String cacheKey) {
    	for (Map.Entry<String, Object> entry : cache.entrySet()){
    		String key = entry.getKey();
    	    
    	    if (key.endsWith(cacheKey))
    	    	cache.put(key, null);
    	}
    }

    public void clear() {
        cache.clear();
    }

    public static I18nCache getInstance() {
        if (instance == null) {
            synchronized (monitor) {
                if (instance == null) {
                    instance = new I18nCache();
                }
            }
        }
        return instance;
    }
}
