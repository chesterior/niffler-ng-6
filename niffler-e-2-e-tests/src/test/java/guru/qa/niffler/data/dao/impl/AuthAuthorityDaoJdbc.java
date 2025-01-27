package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.Authority;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthAuthorityEntity... authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            for (AuthAuthorityEntity a : authority) {
                ps.setObject(1, a.getUser());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthAuthorityEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM authority WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthAuthorityEntity authority = new AuthAuthorityEntity();
                    authority.setId(rs.getObject("id", UUID.class));
                    authority.setUser(rs.getObject("user_id", AuthUserEntity.class));
                    authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                    return Optional.of(authority);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthAuthorityEntity> findByUserId(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM authority WHERE username = ?"
        )) {
            ps.setObject(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AuthAuthorityEntity authority = new AuthAuthorityEntity();
                    authority.setId(rs.getObject("id", UUID.class));
                    authority.setUser(rs.getObject("user_id", AuthUserEntity.class));
                    authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                    return Optional.of(authority);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(AuthAuthorityEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM authority WHERE id = ?"
        )) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
