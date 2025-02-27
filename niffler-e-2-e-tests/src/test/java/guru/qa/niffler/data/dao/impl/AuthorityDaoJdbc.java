package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthorityDaoJdbc implements AuthorityDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO authority (user_id, authority) VALUES (?, ?)")
        ) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUser().getId());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    public Optional<AuthorityEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthorityEntity authority = new AuthorityEntity();
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

    @Nonnull
    @Override
    public Optional<AuthorityEntity> findByUserId(UUID username) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority WHERE username = ?"
        )) {
            ps.setObject(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AuthorityEntity authority = new AuthorityEntity();
                    authority.setId(rs.getObject("id", UUID.class));
                    authority.setId(rs.getObject("user_id", UUID.class));
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
    public void remove(AuthorityEntity user) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM authority WHERE id = ?"
        )) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    public List<AuthorityEntity> findAll() {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority")) {
            try (ResultSet rs = ps.executeQuery()) {
                List<AuthorityEntity> authorities = new ArrayList<>();
                while (rs.next()) {
                    AuthorityEntity au = new AuthorityEntity();
                    au.setId(rs.getObject("id", UUID.class));
                    au.setId(rs.getObject("user_id", UUID.class));
                    au.setAuthority(Authority.valueOf(rs.getString("authority")));
                    authorities.add(au);
                }
                return authorities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
