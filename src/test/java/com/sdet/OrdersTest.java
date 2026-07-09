package com.sdet;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.sdet.OrderBuilder.anOrder;
import static org.junit.jupiter.api.Assertions.*;

class OrdersTest {

    private static final String URL = System.getProperty("db.url", "jdbc:mysql://localhost:3306/orders_test");
    private static final String USER = System.getProperty("db.user", "root");
    private static final String PASSWORD = System.getProperty("db.password", "root");

    static OrderRepository repository;
    static OrderFactory factory;

    @BeforeAll
    static void migrateSchema() {
        Flyway.configure().dataSource(URL, USER, PASSWORD).locations("classpath:com/sdet").load().migrate();
        repository = new OrderRepository(URL, USER, PASSWORD);
        factory = new OrderFactory(repository);
    }

    @BeforeEach
    void resetTables() {
        repository.resetMutableTables();
    }

    @Test
    void flywaySeedsReferenceDataButNoOrders() {
        assertEquals(4, repository.referenceStatusCount());
        assertEquals(0, repository.count());
    }

    @Test
    void factoryPersistsBuilderData() {
        long id = factory.persisted(anOrder().withQuantity(3));
        assertTrue(id > 0);
        assertEquals(1, repository.count());
    }

    @Test
    void countsOnlyThisTestsOrders() {
        factory.persisted(anOrder());
        factory.persisted(anOrder().withSku("SKU-RET-202").withQuantity(2));
        assertEquals(2, repository.count());
    }

    @Test
    void resetMakesTestsIndependent() {
        assertEquals(0, repository.count());
        factory.persisted(anOrder().refunded());
        assertEquals(1, repository.count());
        assertEquals(1, repository.countByStatus("REFUNDED"));
    }
}