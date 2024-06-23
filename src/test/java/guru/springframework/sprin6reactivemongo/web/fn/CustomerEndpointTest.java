package guru.springframework.sprin6reactivemongo.web.fn;

import guru.springframework.sprin6reactivemongo.model.CustomerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class CustomerEndpointTest {

    @Autowired
    WebTestClient webTestClient;
    @Test
    void testPatchIdFound() {
        CustomerDTO customerDTO = getSavedTestCustomer();
        customerDTO.setCustomerName("New Customer Name");
        webTestClient.patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testPatchIdNotFound() {
        webTestClient.patch()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(getSCustomerDto()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void testUpdateCustomer() {
        CustomerDTO customerDTO = getSavedTestCustomer();
        customerDTO.setCustomerName("New Customer Name");
        webTestClient.put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateNotFound() {
        webTestClient.put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(getSCustomerDto()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateBadRequest() {
        CustomerDTO customerDTO = getSavedTestCustomer();
        customerDTO.setCustomerName("");
        webTestClient.put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Order(999)
    @Test
    void testDeleteCustomer(){
        CustomerDTO dto = getSavedTestCustomer();
       webTestClient.delete()
               .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, dto.getId())
               .exchange()
               .expectStatus().isNoContent();
    }
    @Test
    void testDeleteNotFound() {
        webTestClient.delete()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(2)
    void testListCustomers() {
        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").value(greaterThan(3));
    }

    @Test
    void testCreateCustomer() {

        webTestClient.post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(getSCustomerDto()), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectHeader().exists("Location");
    }

    @Test
    @Order(1)
    void testGetById() {
        CustomerDTO customerDTO = getSavedTestCustomer();
        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.customerName").isEqualTo("Test Customer");
    }

    @Test
    void testGetByIdNotFound() {
        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    public CustomerDTO getSavedTestCustomer() {
        FluxExchangeResult<CustomerDTO> customerDTOFluxExchangeResult = webTestClient.post()
                .uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(getSCustomerDto()), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .returnResult(CustomerDTO.class);
        List<String> location = customerDTOFluxExchangeResult.getResponseHeaders().get("Location");
        return webTestClient.get().uri(location.get(0))
                .exchange()
                .returnResult(CustomerDTO.class)
                .getResponseBody().blockFirst();
    }

    public static CustomerDTO getSCustomerDto() {
        return CustomerDTO.builder().customerName("Test Customer").build();
    }
}
