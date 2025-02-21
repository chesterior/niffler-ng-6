package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;
import java.util.UUID;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    //JDBC
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    //RepositoryJdbc
    private final SpendRepositoryJdbc spendRepositoryJdbc = new SpendRepositoryJdbc();

    //RepositoryHibernate
    private final SpendRepositoryHibernate spendRepositoryHibernate = new SpendRepositoryHibernate();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(spendDao.create(spendEntity)
                    );
                }
        );
    }

    @Override
    public SpendJson createSpendRepositoryHibernate(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            if (spendEntity.getCategory().getId() != null) {
                spendEntity.setCategory(spendRepositoryHibernate.update(spendEntity).getCategory());
            } else {
                CategoryEntity categoryEntity = spendRepositoryHibernate.createCategory(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(spendRepositoryHibernate.create(spendEntity));
        });
    }

    @Override
    public SpendJson updateSpendRepositoryHibernate(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            SpendEntity updateEntity = spendRepositoryHibernate.update(spendEntity);
            return SpendJson.fromEntity(updateEntity);
        });
    }

    @Override
    public CategoryJson createCategoryRepositoryHibernate(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            CategoryEntity updateEntity = spendRepositoryHibernate.createCategory(categoryEntity);
            return CategoryJson.fromEntity(updateEntity);
        });
    }

    @Override
    public Optional<SpendJson> findSpendByIdRepositoryHibernate(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> optionalSpendEntity = spendRepositoryHibernate.findSpendById(id);
            return optionalSpendEntity.map(SpendJson::fromEntity);
        });
    }

    @Override
    public Optional<CategoryJson> findCategoryByIdRepositoryHibernate(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity = spendRepositoryHibernate.findCategoryById(id);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    @Override
    public Optional<SpendJson> findByUsernameAndSpendDescriptionRepositoryHibernate(String username, String description) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> optionalSpendEntity = spendRepositoryHibernate
                    .findByUsernameAndSpendDescription(username, description);
            return optionalSpendEntity.map(SpendJson::fromEntity);
        });
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndSpendNameRepositoryHibernate(String username, String name) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity = spendRepositoryHibernate
                    .findCategoryByUsernameAndSpendName(username, name);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    @Override
    public void removeSpendRepositoryHibernate(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            spendRepositoryHibernate.removeSpend(spendEntity);
            return null;
        });
    }

    @Override
    public void removeCategoryRepositoryHibernate(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            spendRepositoryHibernate.removeCategory(categoryEntity);
            return null;
        });
    }
}
