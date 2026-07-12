package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookingServiceImplTest {

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
    void createBooking_shouldReturnBookingDto() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());

        assertNotNull(createdBooking);
        assertNotNull(createdBooking.getId());
        assertEquals("WAITING", createdBooking.getStatus());
        assertEquals(booker.getId(), createdBooking.getBookerId());
    }

    @Test
    void createBooking_withNonExistentItem_shouldThrowNotFoundException() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(999L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(NotFoundException.class, () -> {
            bookingService.create(bookingDto, booker.getId());
        });
    }

    @Test
    void createBooking_withUnavailableItem_shouldThrowNotFoundException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Unavailable Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(false);
        Long unavailableItemId = itemService.create(itemDto, owner.getId(), null).getId();

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(unavailableItemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(NotFoundException.class, () -> {
            bookingService.create(bookingDto, booker.getId());
        });
    }

    @Test
    void createBooking_byOwner_shouldThrowNotFoundException() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(NotFoundException.class, () -> {
            bookingService.create(bookingDto, owner.getId());
        });
    }

    @Test
    void findById_shouldReturnBooking() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());

        BookingDto foundBooking = bookingService.findById(createdBooking.getId(), booker.getId());

        assertNotNull(foundBooking);
        assertEquals(createdBooking.getId(), foundBooking.getId());
    }

    @Test
    void findById_withNonExistentId_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            bookingService.findById(999L, booker.getId());
        });
    }

    @Test
    void findById_withNonBookerAndNonOwner_shouldThrowNotFoundException() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());

        User anotherUser = new User();
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@test.com");
        User savedAnotherUser = userRepository.save(anotherUser);

        assertThrows(NotFoundException.class, () -> {
            bookingService.findById(createdBooking.getId(), savedAnotherUser.getId());
        });
    }

    @Test
    void updateStatus_approve_shouldReturnApprovedBooking() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());

        BookingDto updatedBooking = bookingService.updateStatus(createdBooking.getId(), owner.getId(), true);

        assertNotNull(updatedBooking);
        assertEquals("APPROVED", updatedBooking.getStatus());
    }

    @Test
    void updateStatus_reject_shouldReturnRejectedBooking() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());

        BookingDto updatedBooking = bookingService.updateStatus(createdBooking.getId(), owner.getId(), false);

        assertNotNull(updatedBooking);
        assertEquals("REJECTED", updatedBooking.getStatus());
    }

    @Test
    void updateStatus_withNonOwner_shouldThrowNotFoundException() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());

        assertThrows(NotFoundException.class, () -> {
            bookingService.updateStatus(createdBooking.getId(), booker.getId(), true);
        });
    }

    @Test
    void updateStatus_withNonExistentId_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            bookingService.updateStatus(999L, owner.getId(), true);
        });
    }

    @Test
    void findAllByBooker_shouldReturnBookingList() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(bookingDto, booker.getId());

        List<BookingDto> bookings = bookingService.findAllByBooker(booker.getId(), State.ALL, 0, 10);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByBooker_withNonExistentUser_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            bookingService.findAllByBooker(999L, State.ALL, 0, 10);
        });
    }

    @Test
    void findAllByOwner_shouldReturnBookingList() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(bookingDto, booker.getId());

        List<BookingDto> bookings = bookingService.findAllByOwner(owner.getId(), State.ALL, 0, 10);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByOwner_withNonExistentUser_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            bookingService.findAllByOwner(999L, State.ALL, 0, 10);
        });
    }

    @Test
    void findAllByBooker_withCurrentState_shouldReturnBookings() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().minusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());
        bookingService.updateStatus(createdBooking.getId(), owner.getId(), true);

        List<BookingDto> bookings = bookingService.findAllByBooker(booker.getId(), State.CURRENT, 0, 10);

        assertNotNull(bookings);
    }

    @Test
    void findAllByBooker_withPastState_shouldReturnBookings() {
        List<BookingDto> bookings = bookingService.findAllByBooker(booker.getId(), State.PAST, 0, 10);

        assertNotNull(bookings);
    }

    @Test
    void findAllByBooker_withFutureState_shouldReturnBookings() {
        List<BookingDto> bookings = bookingService.findAllByBooker(booker.getId(), State.FUTURE, 0, 10);

        assertNotNull(bookings);
    }

    @Test
    void findAllByBooker_withWaitingState_shouldReturnBookings() {
        List<BookingDto> bookings = bookingService.findAllByBooker(booker.getId(), State.WAITING, 0, 10);

        assertNotNull(bookings);
    }

    @Test
    void findAllByBooker_withRejectedState_shouldReturnBookings() {
        List<BookingDto> bookings = bookingService.findAllByBooker(booker.getId(), State.REJECTED, 0, 10);

        assertNotNull(bookings);
    }

    @Test
    void findAllByOwner_withCurrentState_shouldReturnBookings() {
        List<BookingDto> bookings = bookingService.findAllByOwner(owner.getId(), State.CURRENT, 0, 10);

        assertNotNull(bookings);
    }

    @Test
    void findAllByOwner_withPastState_shouldReturnBookings() {
        List<BookingDto> bookings = bookingService.findAllByOwner(owner.getId(), State.PAST, 0, 10);

        assertNotNull(bookings);
    }

    @Test
    void findAllByOwner_withFutureState_shouldReturnBookings() {
        List<BookingDto> bookings = bookingService.findAllByOwner(owner.getId(), State.FUTURE, 0, 10);

        assertNotNull(bookings);
    }

    @Test
    void findAllByOwner_withWaitingState_shouldReturnBookings() {
        List<BookingDto> bookings = bookingService.findAllByOwner(owner.getId(), State.WAITING, 0, 10);

        assertNotNull(bookings);
    }

    @Test
    void findAllByOwner_withRejectedState_shouldReturnBookings() {
        List<BookingDto> bookings = bookingService.findAllByOwner(owner.getId(), State.REJECTED, 0, 10);

        assertNotNull(bookings);
    }
}