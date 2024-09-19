package online.store.book.dto.order;

import jakarta.validation.constraints.NotNull;
import online.store.book.model.Order;

public record OrderUpdateRequestDto(
        @NotNull
        Order.Status status
) {
}
