package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UdUserRepository;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;


public class UdUserRepositoryJdbc implements UdUserRepository {

    private final UdUserDao userdataUserDao = new UdUserDaoJdbc();

    @Nonnull
    @Override
    public UserEntity create(UserEntity user) {
        return userdataUserDao.create(user);
    }

    @Nonnull
    @Override
    public UserEntity update(UserEntity user) {
        return userdataUserDao.update(user);
    }

    @Nonnull
    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userdataUserDao.findById(id);
    }

    @Nonnull
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userdataUserDao.findByUsername(username);
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        requester.addFriends(FriendshipStatus.PENDING, addressee);
        userdataUserDao.update(requester);
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        addressee.addFriends(FriendshipStatus.PENDING, requester);
        userdataUserDao.update(addressee);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
        userdataUserDao.update(requester);
        userdataUserDao.update(addressee);
    }

    @Override
    public void remove(UserEntity user) {
        userdataUserDao.remove(user);
    }
}
