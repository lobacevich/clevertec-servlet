package by.clevertec.lobacevich.cache.impl;

import by.clevertec.lobacevich.cache.Cache;
import by.clevertec.lobacevich.entity.User;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUCache implements Cache {

    private final Map<Long, User> map;
    private final Deque<User> deque;
    private int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.deque = new LinkedList<>();
    }

    @Override
    public User getById(Long id) {
        if (map.containsKey(id)) {
            User user = map.get(id);
            deque.remove(user);
            deque.addLast(user);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void put(User user) {
        Long userId = user.getId();
        if (map.containsKey(userId)) {
            User oldUser = map.get(user.getId());
            deque.remove(oldUser);
            deque.addLast(user);
            map.put(userId, user);
        } else {
            map.put(userId, user);
            deque.addLast(user);
            if (deque.size() > this.capacity) {
                User oldUser = deque.getFirst();
                map.remove(oldUser.getId());
                deque.removeFirst();
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (map.containsKey(id)) {
            User user = map.get(id);
            map.remove(id);
            deque.remove(user);
            return true;
        } else {
            return false;
        }
    }
}
