package com.backbase.testing.dataloader;

import com.backbase.testing.dataloader.healthchecks.EntitlementsHealthCheck;
import com.backbase.testing.dataloader.healthchecks.ProductSummaryHealthCheck;
import com.backbase.testing.dataloader.healthchecks.TransactionsHealthCheck;
import com.backbase.testing.dataloader.setup.BankSetup;
import com.backbase.testing.dataloader.setup.ServiceAgreementsSetup;
import com.backbase.testing.dataloader.setup.UsersSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Runner {

    public static void main(String[] args) throws IOException {
        final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");

        BankSetup bankSetup = new BankSetup();
        UsersSetup usersSetup = new UsersSetup();
        ServiceAgreementsSetup serviceAgreementsSetup = new ServiceAgreementsSetup();
        EntitlementsHealthCheck entitlementsHealthCheck = new EntitlementsHealthCheck();
        ProductSummaryHealthCheck productSummaryHealthCheck = new ProductSummaryHealthCheck();
        TransactionsHealthCheck transactionsHealthCheck = new TransactionsHealthCheck();

        entitlementsHealthCheck.checkEntitlementsServicesHealth();
        productSummaryHealthCheck.checkProductSummaryServicesHealth();
        transactionsHealthCheck.checkTransactionsServicesHealth();

        Instant start = Instant.now();

        bankSetup.setupBankWithEntitlementsAdminAndProducts();
        usersSetup.setupUsersWithAndWithoutFunctionDataGroupsPrivileges();
        serviceAgreementsSetup.setupCustomServiceAgreements();

        bankSetup.setupBankNotifications();
        usersSetup.setupContactsPerUser();
        usersSetup.setupPaymentsPerUser();
        usersSetup.setupConversationsPerUser();

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long minutes = duration.getSeconds() / 60;
        long seconds = duration.getSeconds() % 60;
        LOGGER.info("Time to ingest data was " + minutes + " minutes and " + seconds + " seconds");
    }
}
