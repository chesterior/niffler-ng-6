package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJDBC;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.repository.UdUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UdUserRepositorySpringJdbc implements UdUserRepository {

    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    private final UdUserDao userdataUserDao = new UdUserDaoSpringJDBC();


    @Nonnull
    @Override
    public UserEntity create(UserEntity user) {
        return userdataUserDao.create(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id =?",
                        UdUserEntityRowMapper.instance, id
                )
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE username =?",
                        UdUserEntityRowMapper.instance, username
                )
        );
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        jdbcTemplate.update(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)",
                requester.getId(),
                addressee.getId(),
                FriendshipStatus.PENDING.name(),
                Timestamp.valueOf(LocalDateTime.now())
        );
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        jdbcTemplate.update(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)",
                addressee.getId(),
                requester.getId(),
                FriendshipStatus.PENDING.name(),
                Timestamp.valueOf(LocalDateTime.now())
        );
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        String sql = "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[] firstPair = {
                requester.getId(),
                addressee.getId(),
                FriendshipStatus.ACCEPTED.name(),
                now
        };
        Object[] secondPair = {
                addressee.getId(),
                requester.getId(),
                FriendshipStatus.ACCEPTED.name(),
                now
        };
        List<Object[]> batchArgs = Arrays.asList(firstPair, secondPair);
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public void remove(UserEntity user) {
        jdbcTemplate.update(
                "DELETE FROM \"user\" WHERE id =?", user.getId()
        );
    }
}
