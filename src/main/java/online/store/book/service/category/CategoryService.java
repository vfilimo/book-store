package online.store.book.service.category;

import java.util.List;
import online.store.book.dto.category.CategoryDto;
import online.store.book.dto.category.CreteCategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto save(CreteCategoryRequestDto categoryRequestDto);

    CategoryDto update(Long id, CreteCategoryRequestDto categoryRequestDto);

    void deleteById(Long id);
}
