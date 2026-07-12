package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              ItemRepository itemRepository,
                              UserService userService) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public BookingDto create(BookingDto bookingDto, Long bookerId) {
        User booker = userService.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("User with id " + bookerId + " not found"));

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item with id " + bookingDto.getItemId() + " not found"));

        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Item with id " + item.getId() + " is not available for booking");
        }

        if (item.getOwner().getId().equals(bookerId)) {
            throw new NotFoundException("User with id " + bookerId + " is the owner of item with id " + item.getId());
        }

        Booking booking = BookingMapper.toBooking(bookingDto, item, booker);
        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingDto updateStatus(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("User with id " + ownerId + " is not the owner of item");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalStateException("Booking status is not WAITING");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    public BookingDto findById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));

        boolean isBooker = booking.getBooker().getId().equals(userId);
        boolean isOwner = booking.getItem().getOwner().getId().equals(userId);

        if (!isBooker && !isOwner) {
            throw new NotFoundException("User with id " + userId + " has no access to booking with id " + bookingId);
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> findAllByBooker(Long bookerId, State state, int from, int size) {
        userService.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("User with id " + bookerId + " not found"));

        Pageable pageable = PageRequest.of(from / Math.max(1, size), Math.max(1, size));
        List<Booking> bookings = getBookingsByState(bookerId, state, pageable, false);

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findAllByOwner(Long ownerId, State state, int from, int size) {
        userService.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));

        Pageable pageable = PageRequest.of(from / Math.max(1, size), Math.max(1, size));
        List<Booking> bookings = getBookingsByState(ownerId, state, pageable, true);

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<Booking> getBookingsByState(Long userId, State state, Pageable pageable, boolean isOwner) {
        LocalDateTime now = LocalDateTime.now();
        if (isOwner) {
            switch (state) {
                case ALL:
                    return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId, pageable);
                case CURRENT:
                    return bookingRepository.findCurrentByOwner(userId, now, pageable);
                case PAST:
                    return bookingRepository.findPastByOwner(userId, now, pageable);
                case FUTURE:
                    return bookingRepository.findFutureByOwner(userId, now, pageable);
                case WAITING:
                    return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, pageable);
                case REJECTED:
                    return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, pageable);
                default:
                    throw new IllegalArgumentException("Unknown state: " + state);
            }
        } else {
            switch (state) {
                case ALL:
                    return bookingRepository.findAllByBookerIdOrderByStartDesc(userId, pageable);
                case CURRENT:
                    return bookingRepository.findCurrentByBooker(userId, now, pageable);
                case PAST:
                    return bookingRepository.findPastByBooker(userId, now, pageable);
                case FUTURE:
                    return bookingRepository.findFutureByBooker(userId, now, pageable);
                case WAITING:
                    return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, pageable);
                case REJECTED:
                    return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, pageable);
                default:
                    throw new IllegalArgumentException("Unknown state: " + state);
            }
        }
    }
}