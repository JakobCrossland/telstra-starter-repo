package stepDefinitions;

import Entity.SimCardTransaction;
import Entity.SimCardTransactionRepo;
import Controller.SIMCardController.SIMCardRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SimCardActivatorStepDefinitions {

    @Autowired
    private SimCardTransactionRepo repo; // could not autowire?

    @Autowired
    private RestTemplate restTemplate;

    private ResponseEntity<String> response;

    @Given("a SIM card with ICCID {string} and customer email {string}")
    public void given_sim(String iccid, String email) {
        SimCardTransactionRepo repo = new SimCardTransactionRepo() {
            @Override
            public List<SimCardTransaction> findAll() {
                return List.of();
            }

            @Override
            public List<SimCardTransaction> findAll(Sort sort) {
                return List.of();
            }

            @Override
            public Page<SimCardTransaction> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public List<SimCardTransaction> findAllById(Iterable<Long> longs) {
                return List.of();
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(SimCardTransaction entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends SimCardTransaction> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends SimCardTransaction> S save(S entity) {
                return null;
            }

            @Override
            public <S extends SimCardTransaction> List<S> saveAll(Iterable<S> entities) {
                return List.of();
            }

            @Override
            public Optional<SimCardTransaction> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends SimCardTransaction> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends SimCardTransaction> List<S> saveAllAndFlush(Iterable<S> entities) {
                return List.of();
            }

            @Override
            public void deleteAllInBatch(Iterable<SimCardTransaction> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public SimCardTransaction getOne(Long aLong) {
                return null;
            }

            @Override
            public SimCardTransaction getById(Long aLong) {
                return null;
            }

            @Override
            public SimCardTransaction getReferenceById(Long aLong) {
                return null;
            }

            @Override
            public <S extends SimCardTransaction> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends SimCardTransaction> List<S> findAll(Example<S> example) {
                return List.of();
            }

            @Override
            public <S extends SimCardTransaction> List<S> findAll(Example<S> example, Sort sort) {
                return List.of();
            }

            @Override
            public <S extends SimCardTransaction> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends SimCardTransaction> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends SimCardTransaction> boolean exists(Example<S> example) {
                return false;
            }

            @Override
            public <S extends SimCardTransaction, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
                return null;
            }
        };
    }

    @When("I activate the SIM card")
    public void activate_sim() {
        String actuatorUrl = "http://localhost:8444/actuate";
        SIMCardRequest request = new SIMCardRequest("1255789453849037777", "example@example.com");
        response = restTemplate.postForEntity(actuatorUrl, request, String.class);
    }

    @Then("the activation should be successful")
    public void activation_succesful() {
        assertEquals(HttpStatus.OK, response.getStatusCode());

        SimCardTransaction transaction = repo.findById(1L).orElse(null);
        assertEquals("1255789453849037777", transaction.getIccid());
        assertEquals("example@example.com", transaction.getCustomerEmail());
        assertEquals(true, transaction.isActive());
    }

    @When("I activate the SIM card with a failing ICCID")
    public void activate_failing_iccid() {
        String actuatorUrl = "http://localhost:8444/actuate";
        SIMCardRequest request = new SIMCardRequest("8944500102198304826", "example@example.com");
        response = restTemplate.postForEntity(actuatorUrl, request, String.class);
    }

    @Then("the activation should fail")
    public void activation_fail() {
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        SimCardTransaction transaction = repo.findById(1L).orElse(null);
        assertEquals(null, transaction);
    }
}
