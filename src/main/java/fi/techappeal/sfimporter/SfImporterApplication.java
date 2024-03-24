package fi.techappeal.sfimporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SfImporterApplication implements CommandLineRunner {
    private final SalesforceClient client;

    private static final Logger LOG = LoggerFactory
            .getLogger(SfImporterApplication.class);

    public SfImporterApplication(SalesforceClient client) {
        this.client = client;
    }

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(SfImporterApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        String accessToken = client.login();
        LOG.info(client.getChangedContacts(accessToken));
    }
}
