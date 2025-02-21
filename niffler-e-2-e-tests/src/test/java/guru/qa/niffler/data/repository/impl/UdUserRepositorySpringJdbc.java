package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.repository.UdUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UdUserRepositorySpringJdbc implements UdUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UdUserRepositorySpringJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserEntity create(UserEntity user) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)" +
                            "VALUES (?, ?, ?, ?, ?, ?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());
            return ps;
        }, kh);
        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);
        return user;
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
