package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.util.HeaderConstants;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class ItemRequestController {

    private final ItemRequestService requestService;

    public ItemRequestController(ItemRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestDto dto,
                                 @RequestHeader(HeaderConstants.USER_ID_HEADER) Long requesterId) {
        return requestService.create(dto, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> findAllByRequester(@RequestHeader(HeaderConstants.USER_ID_HEADER) Long requesterId) {
        return requestService.findAllByRequester(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllNotByRequester(@RequestHeader(HeaderConstants.USER_ID_HEADER) Long requesterId) {
        return requestService.findAllNotByRequester(requesterId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@PathVariable Long requestId,
                                   @RequestHeader(HeaderConstants.USER_ID_HEADER) Long userId) {
        return requestService.findById(requestId, userId);
    }
}