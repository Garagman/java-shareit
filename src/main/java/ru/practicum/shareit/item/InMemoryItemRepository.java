package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final AtomicLong itemId = new AtomicLong(0);

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(itemId.incrementAndGet());
            items.put(item.getId(), item);
            return item;
        }
        return items.computeIfPresent(item.getId(), (id, i) -> {
            i.setName(item.getName());
            i.setDescription(item.getDescription());
            i.setAvailable(item.getAvailable());
            return i;
        });
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAllByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        String lowerText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable())
                .filter(item -> (item.getName() != null && item.getName().toLowerCase().contains(lowerText)) ||
                        (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerText)))
                .collect(Collectors.toList());
    }
}