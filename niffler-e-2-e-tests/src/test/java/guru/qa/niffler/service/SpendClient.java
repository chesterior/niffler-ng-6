package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;
import java.util.UUID;

public interface SpendClient {

    SpendJson createSpendRepositoryHibernate(SpendJson spend);

    SpendJson updateSpendRepositoryHibernate(SpendJson spend);

    Optional<SpendJson> findSpendByIdRepositoryHibernate(UUID id);

    Optional<SpendJson> findByUsernameAndSpendDescriptionRepositoryHibernate(String username, String description);

    void removeSpendRepositoryHibernate(SpendJson spend);

    CategoryJson createCategoryRepositoryHibernate(CategoryJson category);

    Optional<CategoryJson> findCategoryByIdRepositoryHibernate(UUID id);

    Optional<CategoryJson> findCategoryByUsernameAndSpendNameRepositoryHibernate(String username, String name);

    void removeCategoryRepositoryHibernate(CategoryJson category);
}
