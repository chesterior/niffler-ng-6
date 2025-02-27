package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthorityEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthorityDaoSpringJDBC implements AuthorityDao {

    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));

    @Override
    public void create(AuthorityEntity... authority) {
        jdbcTemplate.batchUpdate("INSERT INTO authority (user_id, authority) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authority[i].getUser().getId());
                        ps.setString(2, authority[i].getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authority.length;
                    }
                }
        );
    }

    @Nonnull
    @Override
    public Optional<AuthorityEntity> findById(UUID id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id =?",
                        AuthorityEntityRowMapper.instance, id
                )
        );
    }

    @Nonnull
    @Override
    public Optional<AuthorityEntity> findByUserId(UUID username) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE username =?",
                        AuthorityEntityRowMapper.instance, username
                )
        );
    }

    @Override
    public void remove(AuthorityEntity authAuthority) {
        jdbcTemplate.update(
                "DELETE FROM authority WHERE id =?", authAuthority.getId()
        );
    }

    @Nonnull
    @Override
    public List<AuthorityEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM authority", AuthorityEntityRowMapper.instance);
    }
}
