package by.clevertec.lobacevich.cache.factory.impl;

import by.clevertec.lobacevich.cache.Cache;
import by.clevertec.lobacevich.cache.factory.CacheFactory;
import by.clevertec.lobacevich.cache.impl.LFUCache;

public class LFUCacheFactory implements CacheFactory {

    @Override
    public Cache createCache() {
        return new LFUCache();
    }
}
