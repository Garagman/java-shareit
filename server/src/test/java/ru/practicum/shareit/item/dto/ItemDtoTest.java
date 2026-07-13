package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void serialize_shouldIncludeAllFields() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        itemDto.setRequestId(10L);

        assertThat(json.write(itemDto)).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(json.write(itemDto)).extractingJsonPathStringValue("@.name").isEqualTo("Test Item");
        assertThat(json.write(itemDto)).extractingJsonPathBooleanValue("@.available").isTrue();
        assertThat(json.write(itemDto)).extractingJsonPathNumberValue("@.requestId").isEqualTo(10);
    }

    @Test
    void deserialize_shouldParseAllFields() throws Exception {
        String jsonContent = "{\"id\":1,\"name\":\"Test Item\",\"description\":\"Test Description\",\"available\":true,\"requestId\":10}";

        ItemDto itemDto = json.parse(jsonContent).getObject();

        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Test Item");
        assertThat(itemDto.getDescription()).isEqualTo("Test Description");
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getRequestId()).isEqualTo(10L);
    }
}