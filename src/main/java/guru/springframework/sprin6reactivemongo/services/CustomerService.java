package guru.springframework.sprin6reactivemongo.services;

import guru.springframework.sprin6reactivemongo.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerDTO> listCustomers();

    Mono<CustomerDTO> saveNewCustomer(CustomerDTO customerDTO);

    Mono<CustomerDTO> saveNewCustomer(Mono<CustomerDTO> customerDTO);
    Mono<CustomerDTO> getByCustomerId(String customerId);
    Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO);
    Mono<Void> deleteCustomerById(String customerId);
    Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO);
}
