package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoSpringJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));

    @Override
    public SpendEntity create(SpendEntity spend) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, spend.getUsername());
            ps.setDate(2, new Date(spend.getSpendDate().getTime()));
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            ps.executeUpdate();
            return ps;
        }, kh);
        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        spend.setId(generatedKey);
        return spend;
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM spend WHERE id =?",
                        SpendEntityRowMapper.instance, id
                )
        );
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        return jdbcTemplate.query(
                "SELECT * FROM spend WHERE username = ?",
                SpendEntityRowMapper.instance, username
        );
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        jdbcTemplate.update(
                "DELETE FROM spend WHERE id =?", spend.getId()
        );
    }

    @Override
    public List<SpendEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM spend", SpendEntityRowMapper.instance);
    }
}
