package guru.springframework.sprin6reactivemongo.web.fn;

import guru.springframework.sprin6reactivemongo.model.CustomerDTO;
import guru.springframework.sprin6reactivemongo.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.validation.Validator;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class CustomerHandler {
    private final CustomerService customerService;
    private final Validator validator;

    private void validate(CustomerDTO customerDto){
        Errors errors = new BeanPropertyBindingResult(customerDto, "customerDTO");
        validator.validate(customerDto, errors);

        if(errors.hasErrors()){
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> listCustomers(ServerRequest request){
        Flux<CustomerDTO> flux;
        flux = customerService.listCustomers();
        return ServerResponse.ok().body(flux, CustomerDTO.class);
    }

    public Mono<ServerResponse> getCustomerById(ServerRequest request){
        return ServerResponse.ok()
                .body(customerService.getByCustomerId(
                                request.pathVariable("customerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),
                        CustomerDTO.class);
    }
    public Mono<ServerResponse> updateCustomer(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CustomerDTO.class)
                .doOnNext(this::validate)
                .flatMap(customerDTO -> customerService.updateCustomer(
                        serverRequest.pathVariable("customerId"), customerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }


    public Mono<ServerResponse> createNewCustomer(ServerRequest request){
        return customerService.saveNewCustomer(request.bodyToMono(CustomerDTO.class)
                        .doOnNext(this::validate))
                .flatMap(customerDTO -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(CustomerRouterConfig.CUSTOMER_PATH_ID)
                                .build(customerDTO.getId()))
                        .build());
    }

    public Mono<ServerResponse> deleteCustomerById(ServerRequest request){
        return customerService.getByCustomerId(request.pathVariable("customerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(customerDTO -> customerService.deleteCustomerById(customerDTO.getId()))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchCustomer(ServerRequest request){
        return request.bodyToMono(CustomerDTO.class)
                .doOnNext(this::validate)
                .flatMap(customerDTO -> customerService.patchCustomer(
                        request.pathVariable("customerId"), customerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

}
