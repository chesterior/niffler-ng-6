package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoSpringJdbc implements CategoryDao {

    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    private final String url = CFG.spendJdbcUrl();

    @Nonnull
    @Override
    public CategoryEntity create(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                                INSERT INTO category (username, name, archived)
                                VALUES (?, ?, ?)
                            """,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        category.setId(generatedKey);
        return category;
    }

    @Nonnull
    @Override
    public CategoryEntity update(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        jdbcTemplate.update("""
                          UPDATE "category"
                            SET name     = ?,
                                archived = ?
                            WHERE id = ?
                        """,
                category.getName(),
                category.isArchived(),
                category.getId()
        );
        return category;
    }

    @Nonnull
    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM category WHERE id =?",
                        CategoryEntityRowMapper.instance, id
                )
        );
    }

    @Nonnull
    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM category WHERE username = ? AND name = ?",
                        CategoryEntityRowMapper.instance, username, categoryName
                )
        );
    }

    @Nonnull
    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        return jdbcTemplate.query(
                "SELECT * FROM category WHERE username = ?",
                CategoryEntityRowMapper.instance, username
        );
    }

    @Override
    public void remove(CategoryEntity category) {
        jdbcTemplate.update(
                "DELETE FROM category WHERE id =?", category.getId()
        );
    }

    @Nonnull
    @Override
    public List<CategoryEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM category", CategoryEntityRowMapper.instance);
    }
}
