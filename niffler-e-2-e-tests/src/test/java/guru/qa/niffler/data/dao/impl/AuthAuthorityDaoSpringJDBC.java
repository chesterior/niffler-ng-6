package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.AuthAuthorityEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoSpringJDBC implements AuthAuthorityDao {

    private final DataSource dataSource;

    public AuthAuthorityDaoSpringJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(AuthAuthorityEntity... authority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.batchUpdate("INSERT INTO autority (user_id, authority) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authority[i].getUser());
                        ps.setObject(2, authority[i].getAuthority());
                    }

                    @Override
                    public int getBatchSize() {
                        return authority.length;
                    }
                }
        );
    }

    @Override
    public Optional<AuthAuthorityEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<AuthAuthorityEntity> findByUserId(String username) {
        return Optional.empty();
    }

    @Override
    public void deleteById(AuthAuthorityEntity authAuthority) {

    }
}
