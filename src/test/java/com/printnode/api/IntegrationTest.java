package com.printnode.api;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {

    private static final String TEST_API_KEY_NAME = "API_KEY";

    public PrintNodeClient makeClient() throws InterruptedException {
        Thread.sleep(1000);
        Auth anAuth = new Auth();
        anAuth.setApiKey(System.getenv("API_KEY"));
        PrintNodeClient aClient = new PrintNodeClient(anAuth);
        aClient.setApiUrl("https://api.printnode.com");
        return aClient;
    }

    @Test
    @EnabledIfEnvironmentVariable(named = TEST_API_KEY_NAME, matches = ".+")
    public void testAuth() throws IOException {
        Auth testAuth = new Auth();
        testAuth.setApiKey(System.getenv(TEST_API_KEY_NAME));

        PrintNodeClient testClient = new PrintNodeClient(testAuth);
        testClient.setApiUrl("https://api.printnode.com");

        Whoami aRequest = testClient.getWhoami();

        boolean test = (aRequest.getCanCreateSubAccounts() == true);

        assertTrue(test, "Failed: Should be int.");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = TEST_API_KEY_NAME, matches = ".+")
    public void testComputers() throws IOException, InterruptedException {
        PrintNodeClient aClient = makeClient();

        Computer[] computers = aClient.getComputers("");

        assertTrue(computers[0] instanceof Computer, "Failed: Should be 'created'");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = TEST_API_KEY_NAME, matches = ".+")
    public void testPrinters() throws InterruptedException {
        PrintNodeClient aClient = makeClient();

        List<Printer> printers = aClient.getPrinters("");

        assertTrue(printers.get(0).getComputer() instanceof Computer, "Failed: Should be instance of Computer");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = TEST_API_KEY_NAME, matches = ".+")
    public void testPrintJobs() throws IOException, InterruptedException {
        PrintNodeClient aClient = makeClient();

        PrintJob[] printjobs = aClient.getPrintJobs("");

        assertTrue(printjobs[0].getPrinter().getComputer() instanceof Computer, "Failed: should be instance of Computer");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = TEST_API_KEY_NAME, matches = ".+")
    public void testScales() throws IOException, InterruptedException {
        PrintNodeClient aClient = makeClient();

        Scale aScale = aClient.getScales(0)[0];

        assertTrue(aScale.getProduct() != null, "Failed: should exist");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = TEST_API_KEY_NAME, matches = ".+")
    public void testSubmitPrintJob() throws IOException, InterruptedException {
        byte[] content = IOUtils.toByteArray(this.getClass().getResourceAsStream("/sample.pdf"));
        PrintNodeClient aClient = makeClient();

        Printer aPrinter = aClient.getPrinters("").get(0);

        PrintJobJson myPrintJobCreation = PrintJobJson.ofPdfContent(aPrinter.getId(), "PrintNode-Java", content, "From PrintNode-Java Client");
        long response = aClient.createPrintJob(myPrintJobCreation);
    }

    @Test
    @EnabledIfEnvironmentVariable(named = TEST_API_KEY_NAME, matches = ".+")
    public void testAccounts() throws IOException, InterruptedException {
        PrintNodeClient aClient = makeClient();

        CreateAccountJson accountInfo = new CreateAccountJson("A", "Person", "AnEmail@SomeEmailProvider.Email", "AStrongPassword");

        accountInfo.setApiKeys(new String[]{"abc", "def"});

        CreateAccountObject accountResponse = aClient.createAccount(accountInfo);

        String firstname = accountResponse.getAccount().getFirstname();

        PrintNodeClient childClient = makeClient();

        childClient.setChildAccountById(accountResponse.getAccount().getId());

        Account newAccountInfo = accountResponse.getAccount();

        newAccountInfo.setFirstname("The");

        newAccountInfo.setId(-1);

        Whoami ModifiedAccount = childClient.modifyAccount(newAccountInfo);

        assertTrue(!(ModifiedAccount.getFirstname().equals(firstname)), "Failed: account was not modified");

        boolean deleteAccount = childClient.deleteAccount();

        assertTrue(deleteAccount, "Failed: account was not deleted, so account did not exist");
    }


    @Test
    @EnabledIfEnvironmentVariable(named = TEST_API_KEY_NAME, matches = ".+")
    public void testTagsFunctionality() throws IOException, InterruptedException {
        PrintNodeClient aClient = makeClient();

        String tag = aClient.createTag("AnAmazingTag", "AGreatValue");

        assertTrue(tag.equals("created") || tag.equals("update"), "Failed: should equal created.");

        String tagGotten = aClient.getTags("AnAmazingTag");

        assertTrue(tagGotten.equals("AGreatValue"), "Failed: Was not 'AGreatValue'");

        boolean tagDelete = aClient.deleteTag("AnAmazingTag");

        assertTrue(tagDelete, "Failed: was false or something else");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = TEST_API_KEY_NAME, matches = ".+")
    public void testAPIKeyFunctionality() throws IOException, InterruptedException {
        PrintNodeClient aClient = makeClient();

        CreateAccountJson accountInfo = new CreateAccountJson("A", "Person", "AnEmail@SomeEmailProvider.Email", "AStrongPassword");

        CreateAccountObject accountResponse = aClient.createAccount(accountInfo);

        PrintNodeClient childClient = makeClient();

        childClient.setChildAccountById(accountResponse.getAccount().getId());

        String apiKey = childClient.createApiKey("Development");

        String apiKeyGotten = childClient.getApiKeys("Development");

        assertTrue(apiKeyGotten.equals(apiKey), "Failed: gotten apikey did not equal apikey created");

        boolean apiKeyDelete = childClient.deleteApiKey("Development");

        assertTrue(apiKeyDelete, "Failed: was false or something else");

        childClient.deleteAccount();
    }

    @Test
    @EnabledIfEnvironmentVariable(named = TEST_API_KEY_NAME, matches = ".+")
    public void testGetLatestClient() throws IOException, InterruptedException {
        PrintNodeClient aClient = makeClient();

        Download latestClient = aClient.getLatestClient("windows");

        assertTrue(latestClient.getFilename().endsWith("exe"), "Failed: was not an .exe file");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = TEST_API_KEY_NAME, matches = ".+")
    public void testClients() throws IOException, InterruptedException {
        PrintNodeClient aClient = makeClient();

        Client[] clients = aClient.getClients("");

        int[] clientsDisabled = aClient.modifyClientDownloads(Integer.toString(clients[0].getId()), false);

        assertTrue(clientsDisabled[0] == clients[0].getId(), "Failed: client was not modified");

        int[] clientsEnabled = aClient.modifyClientDownloads(Integer.toString(clients[0].getId()), true);
    }

}
