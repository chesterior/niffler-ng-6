package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.SpendJson;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    //RepositoryJdbc
    private final SpendRepositoryJdbc spendRepositoryJdbc = new SpendRepositoryJdbc();

    //RepositoryHibernate
    private final SpendRepositoryHibernate spendRepositoryHibernate = new SpendRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpendRepositoryHibernate(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            if (spendEntity.getCategory().getId() != null) {
                spendEntity.setCategory(spendRepositoryHibernate.update(spendEntity).getCategory());
            } else {
                CategoryEntity categoryEntity = spendRepositoryHibernate.createCategory(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(spendRepositoryHibernate.create(spendEntity));
        });
    }
}
