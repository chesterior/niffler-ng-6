package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
    CategoryEntity create(CategoryEntity entity);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);

    List<CategoryEntity> findAllByUsername(String username);

    void deleteCategory(CategoryEntity category);
}
