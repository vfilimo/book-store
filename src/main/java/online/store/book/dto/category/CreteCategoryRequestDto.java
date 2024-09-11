package online.store.book.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CreteCategoryRequestDto(
        @NotBlank
        String name,
        String description) {
}
