package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Service
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long ownerId);

    @Query("select i from Item i " +
            "where i.available = true and (lower(i.name) like concat('%', ?1, '%') " +
            "or lower(i.description) like concat('%', ?1, '%'))")
    List<Item> searchItems(String searchText);
}
