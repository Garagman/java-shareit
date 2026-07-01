package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.util.HeaderConstants;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader(HeaderConstants.USER_ID_HEADER) Long ownerId) {
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable Long itemId,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader(HeaderConstants.USER_ID_HEADER) Long ownerId) {
        return itemService.update(itemDto, itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text,
                                @RequestHeader(HeaderConstants.USER_ID_HEADER) Long userId) {
        return itemService.search(text, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId,
                            @RequestHeader(HeaderConstants.USER_ID_HEADER) Long userId) {
        return itemService.findById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> findAllByOwnerId(@RequestHeader(HeaderConstants.USER_ID_HEADER) Long ownerId) {
        return itemService.findAllByOwnerId(ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto,
                                 @RequestHeader(HeaderConstants.USER_ID_HEADER) Long authorId) {
        return itemService.addComment(commentDto, itemId, authorId);
    }
}