package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        BookingDto requestDto = new BookingDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto responseDto = new BookingDto();
        responseDto.setId(1L);
        responseDto.setItemId(1L);
        responseDto.setBookerId(2L);
        responseDto.setStatus("WAITING");

        when(bookingService.create(any(BookingDto.class), eq(2L))).thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void findById_shouldReturnBooking() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStatus("WAITING");

        when(bookingService.findById(eq(1L), eq(2L))).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateStatus_shouldReturnUpdatedBooking() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStatus("APPROVED");

        when(bookingService.updateStatus(eq(1L), eq(1L), eq(true))).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void findAllByBooker_shouldReturnBookingList() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStatus("WAITING");

        when(bookingService.findAllByBooker(eq(2L), any(State.class), eq(0), eq(10)))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void findAllByOwner_shouldReturnBookingList() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStatus("WAITING");

        when(bookingService.findAllByOwner(eq(1L), any(State.class), eq(0), eq(10)))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}