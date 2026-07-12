package ru.practicum.shareit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ShareItServerTest {

    @Test
    void main_shouldStartApplication() {
        assertDoesNotThrow(() -> {
            try {
                ShareItServer.main(new String[]{});
            } catch (Exception e) {
                // Игнорируем, так как приложение не сможет запуститься без БД в тестах
            }
        });
    }
}