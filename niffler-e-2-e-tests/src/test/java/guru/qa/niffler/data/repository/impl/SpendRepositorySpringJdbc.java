package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositorySpringJdbc implements SpendRepository {

    private static final Config CFG = Config.getInstance();

    private final String url = CFG.spendJdbcUrl();
    private final SpendDao spendDao = new SpendDaoSpringJdbc();
    private final CategoryDao categoryDao = new CategoryDaoSpringJdbc();

    @Nonnull
    @Override
    public SpendEntity create(SpendEntity spend) {
        final UUID categoryId = spend.getCategory().getId();
        if (categoryId == null || categoryDao.findCategoryById(categoryId).isEmpty()) {
            spend.setCategory(
                    categoryDao.create(spend.getCategory())
            );
        }
        return spendDao.create(spend);
    }

    @Nonnull
    @Override
    public SpendEntity update(SpendEntity spend) {
        spendDao.update(spend);
        categoryDao.update(spend.getCategory());
        return spend;
    }

    @Nonnull
    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Nonnull
    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    @Nonnull
    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM category WHERE username = ? and name = ?",
                        CategoryEntityRowMapper.instance,
                        username,
                        name
                )
        );
    }

    @Nonnull
    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return spendDao.findSpendById(id);
    }

    @Nonnull
    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM spend WHERE username = ? and description = ?",
                        SpendEntityRowMapper.instance,
                        username,
                        description
                )
        );
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        jdbcTemplate.update("DELETE FROM spend WHERE id = ?", spend.getId());
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        jdbcTemplate.update("DELETE FROM category WHERE id = ?", category.getId());
    }
}
