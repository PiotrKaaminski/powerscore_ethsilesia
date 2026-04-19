package pl.kaminski.powerscore.client.infrastructure;

import org.springframework.context.annotation.Bean;
import pl.kaminski.powerscore.client.application.ClientFacade;
import pl.kaminski.powerscore.client.application.contract.IClientFacade;
import pl.kaminski.powerscore.client.domain.ClientFactory;
import pl.kaminski.powerscore.client.domain.IClientRepository;
import pl.kaminski.powerscore.client.query.ClientQueryFacade;
import pl.kaminski.powerscore.client.query.IClientQueryRepository;
import pl.kaminski.powerscore.client.query.contract.IClientQueryFacade;

public class ClientConfiguration {
    private final IClientQueryRepository queryRepository;
    private final IClientRepository repository;
    private final ClientFactory factory;

    ClientConfiguration(ClientQueryJpaRepository queryJpaRepository, ClientJpaRepository jpaRepository) {
        this.queryRepository = new ClientQueryRepository(queryJpaRepository);
        this.repository = new ClientRepository(jpaRepository);
        this.factory = new ClientFactory();
    }

    @Bean
    IClientQueryFacade clientQueryFacade() {
        return new ClientQueryFacade(queryRepository);
    }

    @Bean
    IClientFacade clientFacade() {
        return new ClientFacade(repository, factory);
    }

    @Bean
    IClientRepository clientRepository() {
        return repository;
    }
}
