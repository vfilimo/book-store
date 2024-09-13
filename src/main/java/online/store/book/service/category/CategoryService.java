package online.store.book.service.category;

import java.util.List;
import online.store.book.dto.category.CategoryDto;
import online.store.book.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryRequestDto);

    CategoryDto update(Long id, CreateCategoryRequestDto categoryRequestDto);

    void deleteById(Long id);
}
