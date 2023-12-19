package by.clevertec.lobacevich.cache.impl;

import by.clevertec.lobacevich.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LRUCacheTest {

    private final LRUCache cache = new LRUCache();

    @Test
    void getByIdShouldGiveUserById() {
        User expected = CacheTestData.getUser1();
        cache.put(expected);

        User actual = cache.getById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void putShouldRemoveFirstAddedUser() {
        cache.put(CacheTestData.getUser1());
        cache.put(CacheTestData.getUser2());
        cache.put(CacheTestData.getUser3());
        cache.put(CacheTestData.getUser4());

        assertNull(cache.getById(1L));
    }

    @Test
    void deleteShouldDeleteUserById() {
        User user = CacheTestData.getUser1();
        cache.put(user);

        cache.deleteById(1L);

        assertNull(cache.getById(1L));
    }
}