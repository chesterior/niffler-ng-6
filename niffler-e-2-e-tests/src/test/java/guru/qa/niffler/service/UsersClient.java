package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

public interface UsersClient {

    UserJson createUserRepositoryHibernate(String username, String password);

    void addIncomeInvitationHibernate(UserJson targetUser, int count);

    void addOutcomeInvitationHibernate(UserJson targetUser, int count);

    void addFriendHibernate(UserJson targetUser, int count);
}
