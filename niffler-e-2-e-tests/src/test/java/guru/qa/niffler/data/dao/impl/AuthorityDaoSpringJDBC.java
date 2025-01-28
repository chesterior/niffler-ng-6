package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.entity.AuthorityEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class AuthorityDaoSpringJDBC implements AuthorityDao {

    private final DataSource dataSource;

    public AuthorityDaoSpringJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(AuthorityEntity... authority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate("INSERT INTO authority (user_id, authority) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authority[i].getUserId());
                        ps.setString(2, authority[i].getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authority.length;
                    }
                }
        );
    }

    @Override
    public Optional<AuthorityEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<AuthorityEntity> findByUserId(String username) {
        return Optional.empty();
    }

    @Override
    public void deleteById(AuthorityEntity authAuthority) {

    }
}
