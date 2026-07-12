package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(ItemRequestDto dto, Long requesterId);

    List<ItemRequestDto> findAllByRequester(Long requesterId);

    List<ItemRequestDto> findAllNotByRequester(Long requesterId);

    ItemRequestDto findById(Long requestId, Long userId);
}