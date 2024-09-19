package online.store.book.dto.order;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        int quantity
) {
}
