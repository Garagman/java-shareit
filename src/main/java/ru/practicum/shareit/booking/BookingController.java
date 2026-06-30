package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(@RequestBody BookingDto bookingDto,
                             @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        return bookingService.create(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@PathVariable Long bookingId,
                                   @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                   @RequestParam("approved") Boolean approved) {
        return bookingService.updateStatus(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@PathVariable Long bookingId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> findAllByBooker(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                            @RequestParam(defaultValue = "ALL") String state,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        State stateEnum = parseState(state);
        return bookingService.findAllByBooker(bookerId, stateEnum, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                           @RequestParam(defaultValue = "ALL") String state,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        State stateEnum = parseState(state);
        return bookingService.findAllByOwner(ownerId, stateEnum, from, size);
    }

    private State parseState(String state) {
        try {
            return State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}