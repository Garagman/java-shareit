package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    private User owner;
    private User booker;
    private Long itemId;

    @BeforeEach
    void setUp() {
        User newOwner = new User();
        newOwner.setName("Owner");
        newOwner.setEmail("owner@test.com");
        owner = userRepository.save(newOwner);

        User newBooker = new User();
        newBooker.setName("Booker");
        newBooker.setEmail("booker@test.com");
        booker = userRepository.save(newBooker);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
        itemId = itemService.create(itemDto, owner.getId(), null).getId();
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDesc_shouldReturnBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(bookingDto, booker.getId());

        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(owner.getId(), pageable);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByBookerIdOrderByStartDesc_shouldReturnBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(bookingDto, booker.getId());

        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(booker.getId(), pageable);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void findCurrentByOwner_shouldReturnCurrentBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().minusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());
        bookingService.updateStatus(createdBooking.getId(), owner.getId(), true);

        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findCurrentByOwner(owner.getId(), now, pageable);

        assertNotNull(bookings);
    }

    @Test
    void findPastByOwner_shouldReturnPastBookings() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findPastByOwner(owner.getId(), now, pageable);

        assertNotNull(bookings);
    }

    @Test
    void findFutureByOwner_shouldReturnFutureBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(bookingDto, booker.getId());

        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findFutureByOwner(owner.getId(), now, pageable);

        assertNotNull(bookings);
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDesc_shouldReturnWaitingBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(bookingDto, booker.getId());

        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                owner.getId(), BookingStatus.WAITING, pageable);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDesc_shouldReturnRejectedBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());
        bookingService.updateStatus(createdBooking.getId(), owner.getId(), false);

        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                owner.getId(), BookingStatus.REJECTED, pageable);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void findCurrentByBooker_shouldReturnCurrentBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().minusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());
        bookingService.updateStatus(createdBooking.getId(), owner.getId(), true);

        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findCurrentByBooker(booker.getId(), now, pageable);

        assertNotNull(bookings);
    }

    @Test
    void findPastByBooker_shouldReturnPastBookings() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findPastByBooker(booker.getId(), now, pageable);

        assertNotNull(bookings);
    }

    @Test
    void findFutureByBooker_shouldReturnFutureBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(bookingDto, booker.getId());

        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findFutureByBooker(booker.getId(), now, pageable);

        assertNotNull(bookings);
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc_shouldReturnWaitingBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(bookingDto, booker.getId());

        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                booker.getId(), BookingStatus.WAITING, pageable);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc_shouldReturnRejectedBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());
        bookingService.updateStatus(createdBooking.getId(), owner.getId(), false);

        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                booker.getId(), BookingStatus.REJECTED, pageable);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void findPastBookingsByItemAndBooker_shouldReturnPastBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().minusDays(2));
        bookingDto.setEnd(LocalDateTime.now().minusDays(1));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());
        bookingService.updateStatus(createdBooking.getId(), owner.getId(), true);

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findPastBookingsByItemAndBooker(itemId, booker.getId(), now);

        assertNotNull(bookings);
    }

    @Test
    void findAllPastBookingsByItemIds_shouldReturnPastBookings() {
        List<Booking> bookings = bookingRepository.findAllPastBookingsByItemIds(List.of(itemId), LocalDateTime.now());

        assertNotNull(bookings);
    }

    @Test
    void findAllFutureBookingsByItemIds_shouldReturnFutureBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(bookingDto, booker.getId());

        List<Booking> bookings = bookingRepository.findAllFutureBookingsByItemIds(List.of(itemId), LocalDateTime.now());

        assertNotNull(bookings);
    }

    @Test
    void findLastPastBookingByItemId_shouldReturnBooking() {
        var result = bookingRepository.findLastPastBookingByItemId(itemId, LocalDateTime.now());

        assertNotNull(result);
    }

    @Test
    void findNextFutureBookingByItemId_shouldReturnBooking() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(bookingDto, booker.getId());

        var result = bookingRepository.findNextFutureBookingByItemId(itemId, LocalDateTime.now());

        assertNotNull(result);
    }
}