package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemBookingInfo;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository,
                           UserService userService,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Long ownerId) {
        User owner = userService.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));
        Item item = ItemMapper.toItem(itemDto, owner);
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Item with id " + itemId + " not found for user with id " + ownerId);
        }
        ItemMapper.updateItemFields(item, itemDto);
        Item updatedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto findById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));

        ItemBookingInfo lastBooking = null;
        ItemBookingInfo nextBooking = null;
        List<CommentDto> comments = new ArrayList<>();

        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();
            lastBooking = bookingRepository.findLastPastBookingByItemId(itemId, now)
                    .map(b -> new ItemBookingInfo(b.getId(), b.getBooker().getId()))
                    .orElse(null);
            nextBooking = bookingRepository.findNextFutureBookingByItemId(itemId, now)
                    .map(b -> new ItemBookingInfo(b.getId(), b.getBooker().getId()))
                    .orElse(null);
        }

        comments = commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        return ItemMapper.toItemDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemDto> findAllByOwnerId(Long ownerId) {
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        if (items.isEmpty()) {
            return new ArrayList<>();
        }

        LocalDateTime now = LocalDateTime.now();
        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());

        List<Booking> pastBookings = bookingRepository.findAllPastBookingsByItemIds(itemIds, now);
        List<Booking> futureBookings = bookingRepository.findAllFutureBookingsByItemIds(itemIds, now);

        Map<Long, ItemBookingInfo> lastBookingsMap = new HashMap<>();
        Map<Long, ItemBookingInfo> nextBookingsMap = new HashMap<>();

        for (Booking booking : pastBookings) {
            Long itemId = booking.getItem().getId();
            ItemBookingInfo existing = lastBookingsMap.get(itemId);
            if (existing == null || booking.getEnd().isAfter(
                    bookingRepository.findById(existing.getId()).map(Booking::getEnd).orElse(null))) {
                lastBookingsMap.put(itemId, new ItemBookingInfo(booking.getId(), booking.getBooker().getId()));
            }
        }

        for (Booking booking : futureBookings) {
            Long itemId = booking.getItem().getId();
            ItemBookingInfo existing = nextBookingsMap.get(itemId);
            if (existing == null || booking.getStart().isBefore(
                    bookingRepository.findById(existing.getId()).map(Booking::getStart).orElse(null))) {
                nextBookingsMap.put(itemId, new ItemBookingInfo(booking.getId(), booking.getBooker().getId()));
            }
        }

        List<Comment> allComments = commentRepository.findAllByItemIdIn(itemIds);
        Map<Long, List<CommentDto>> commentsMap = allComments.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getItem().getId(),
                        Collectors.mapping(CommentMapper::toCommentDto, Collectors.toList())
                ));

        return items.stream()
                .map(item -> ItemMapper.toItemDto(
                        item,
                        lastBookingsMap.get(item.getId()),
                        nextBookingsMap.get(item.getId()),
                        commentsMap.getOrDefault(item.getId(), new ArrayList<>())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text, Long userId) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.search(text);
        return items.stream()
                .map(item -> ItemMapper.toItemDto(item, null, null, new ArrayList<>()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentDto, Long itemId, Long authorId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));

        User author = userService.findById(authorId)
                .orElseThrow(() -> new NotFoundException("User with id " + authorId + " not found"));

        LocalDateTime now = LocalDateTime.now();
        List<Booking> pastBookings = bookingRepository.findPastBookingsByItemAndBooker(itemId, authorId, now);

        if (pastBookings.isEmpty()) {
            throw new IllegalArgumentException("User with id " + authorId + " has not booked item with id " + itemId);
        }

        Comment comment = CommentMapper.toComment(commentDto, item, author);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(savedComment);
    }
}