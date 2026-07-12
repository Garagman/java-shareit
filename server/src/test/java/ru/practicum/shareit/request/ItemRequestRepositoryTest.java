package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository requestRepository;

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
    void findAllByRequesterIdOrderByCreatedDesc_shouldReturnRequests() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
        requestService.create(requestDto, requester.getId());

        List<ItemRequest> requests = requestRepository.findAllByRequesterIdOrderByCreatedDesc(requester.getId());

        assertNotNull(requests);
        assertFalse(requests.isEmpty());
    }

    @Test
    void findAllByRequesterIdNotOrderByCreatedDesc_shouldReturnOtherRequests() {
        User anotherUser = new User();
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@test.com");
        User savedAnotherUser = userRepository.save(anotherUser);

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
        requestService.create(requestDto, savedAnotherUser.getId());

        List<ItemRequest> requests = requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(requester.getId());

        assertNotNull(requests);
        assertFalse(requests.isEmpty());
    }

    @Test
    void findAllByRequesterIdOrderByCreatedDesc_withNoRequests_shouldReturnEmptyList() {
        List<ItemRequest> requests = requestRepository.findAllByRequesterIdOrderByCreatedDesc(requester.getId());

        assertNotNull(requests);
        assertTrue(requests.isEmpty());
    }

    @Test
    void findById_existingRequest_shouldReturnRequest() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
        ItemRequestDto createdRequest = requestService.create(requestDto, requester.getId());

        var foundRequest = requestRepository.findById(createdRequest.getId());

        assertTrue(foundRequest.isPresent());
        assertEquals(createdRequest.getId(), foundRequest.get().getId());
    }

    @Test
    void findById_nonExistingRequest_shouldReturnEmpty() {
        var foundRequest = requestRepository.findById(999L);

        assertFalse(foundRequest.isPresent());
    }
}