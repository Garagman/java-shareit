package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemRequestServiceImpl(ItemRequestRepository requestRepository,
                                  ItemRepository itemRepository,
                                  UserService userService) {
        this.requestRepository = requestRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public ItemRequestDto create(ItemRequestDto dto, Long requesterId) {
        User requester = userService.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("User with id " + requesterId + " not found"));
        ItemRequest request = ItemRequestMapper.toItemRequest(dto, requester);
        ItemRequest saved = requestRepository.save(request);
        return ItemRequestMapper.toItemRequestDto(saved, List.of());
    }

    @Override
    public List<ItemRequestDto> findAllByRequester(Long requesterId) {
        userService.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("User with id " + requesterId + " not found"));
        List<ItemRequest> requests = requestRepository.findAllByRequesterIdOrderByCreatedDesc(requesterId);
        return mapToDtoWithItems(requests);
    }

    @Override
    public List<ItemRequestDto> findAllNotByRequester(Long requesterId) {
        userService.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("User with id " + requesterId + " not found"));
        List<ItemRequest> requests = requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(requesterId);
        return mapToDtoWithItems(requests);
    }

    @Override
    public ItemRequestDto findById(Long requestId, Long userId) {
        userService.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id " + requestId + " not found"));
        List<Item> items = itemRepository.findAllByRequestId(requestId);
        return ItemRequestMapper.toItemRequestDto(request, items);
    }

    private List<ItemRequestDto> mapToDtoWithItems(List<ItemRequest> requests) {
        return requests.stream()
                .map(req -> ItemRequestMapper.toItemRequestDto(req, List.of()))
                .toList();
    }
}