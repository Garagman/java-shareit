package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService requestService;

    @Autowired
    private UserRepository userRepository;

    private User requester;

    @BeforeEach
    void setUp() {
        User newUser = new User();
        newUser.setName("Test Requester");
        newUser.setEmail("requester@test.com");
        requester = userRepository.save(newUser);
    }

    @Test
    void createRequest_shouldReturnRequestDto() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");

        ItemRequestDto createdRequest = requestService.create(requestDto, requester.getId());

        assertNotNull(createdRequest);
        assertNotNull(createdRequest.getId());
        assertEquals("Need a drill", createdRequest.getDescription());
        assertNotNull(createdRequest.getCreated());
    }

    @Test
    void createRequest_withNonExistentUser_shouldThrowNotFoundException() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");

        assertThrows(NotFoundException.class, () -> {
            requestService.create(requestDto, 999L);
        });
    }

    @Test
    void findAllByRequester_shouldReturnRequestList() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
        requestService.create(requestDto, requester.getId());

        var requests = requestService.findAllByRequester(requester.getId());

        assertNotNull(requests);
        assertFalse(requests.isEmpty());
    }

    @Test
    void findAllByRequester_withNonExistentUser_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            requestService.findAllByRequester(999L);
        });
    }

    @Test
    void findAllNotByRequester_shouldReturnEmptyList() {
        var requests = requestService.findAllNotByRequester(requester.getId());

        assertNotNull(requests);
        assertTrue(requests.isEmpty());
    }

    @Test
    void findAllNotByRequester_shouldReturnOtherRequests() {
        User anotherUser = new User();
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@test.com");
        User savedAnotherUser = userRepository.save(anotherUser);

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
        requestService.create(requestDto, savedAnotherUser.getId());

        var requests = requestService.findAllNotByRequester(requester.getId());

        assertNotNull(requests);
        assertFalse(requests.isEmpty());
    }

    @Test
    void findAllNotByRequester_withNonExistentUser_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            requestService.findAllNotByRequester(999L);
        });
    }

    @Test
    void findById_shouldReturnRequest() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
        ItemRequestDto createdRequest = requestService.create(requestDto, requester.getId());

        ItemRequestDto foundRequest = requestService.findById(createdRequest.getId(), requester.getId());

        assertNotNull(foundRequest);
        assertEquals(createdRequest.getId(), foundRequest.getId());
    }

    @Test
    void findById_withNonExistentId_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            requestService.findById(999L, requester.getId());
        });
    }

    @Test
    void findById_withNonExistentUser_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            requestService.findById(1L, 999L);
        });
    }
}