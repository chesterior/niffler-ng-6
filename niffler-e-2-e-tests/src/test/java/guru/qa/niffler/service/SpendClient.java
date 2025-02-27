package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;
import java.util.UUID;

public interface SpendClient {

    SpendJson createSpend(SpendJson spend);

    SpendJson updateSpend(SpendJson spend);

    Optional<SpendJson> findSpendById(UUID id);

    Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description);

    void removeSpend(SpendJson spend);

    CategoryJson createCategory(CategoryJson category);

    Optional<CategoryJson> findCategoryById(UUID id);

    Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name);

    void removeCategory(CategoryJson category);
}
