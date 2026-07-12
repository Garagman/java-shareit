package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void serialize_shouldIncludeAllFields() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");

        assertThat(json.write(userDto)).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(json.write(userDto)).extractingJsonPathStringValue("@.name").isEqualTo("Test User");
        assertThat(json.write(userDto)).extractingJsonPathStringValue("@.email").isEqualTo("test@example.com");
    }

    @Test
    void deserialize_shouldParseAllFields() throws Exception {
        String jsonContent = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@example.com\"}";

        UserDto userDto = json.parse(jsonContent).getObject();

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("Test User");
        assertThat(userDto.getEmail()).isEqualTo("test@example.com");
    }
}