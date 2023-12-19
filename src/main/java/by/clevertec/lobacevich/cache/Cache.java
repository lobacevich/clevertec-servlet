package by.clevertec.lobacevich.cache;

import by.clevertec.lobacevich.entity.User;

public interface Cache {

    User getById(Long id);

    public void put(User user);

    public boolean deleteById(Long id);
}
