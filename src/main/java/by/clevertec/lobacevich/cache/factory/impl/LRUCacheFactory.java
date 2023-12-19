package by.clevertec.lobacevich.cache.factory.impl;

import by.clevertec.lobacevich.cache.Cache;
import by.clevertec.lobacevich.cache.factory.CacheFactory;
import by.clevertec.lobacevich.cache.impl.LRUCache;

public class LRUCacheFactory implements CacheFactory {

    @Override
    public Cache createCache() {
        return new LRUCache();
    }
}
