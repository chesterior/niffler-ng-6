package guru.qa.niffler.config;

public interface Config {

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }

    static Config getInstanceDocker() {
        return DockerConfig.INSTANCE;
    }

    String frontUrl();

    String authUrl();

    String authJdbcUrl();

    String gatewayUrl();

    String userdataUrl();

    String userdataJdbcUrl();

    String spendUrl();

    String spendJdbcUrl();

    String currencyJdbcUrl();

    String ghUrl();
}
