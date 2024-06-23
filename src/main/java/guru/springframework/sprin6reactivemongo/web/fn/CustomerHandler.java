package guru.springframework.sprin6reactivemongo.web.fn;

import guru.springframework.sprin6reactivemongo.model.CustomerDTO;
import guru.springframework.sprin6reactivemongo.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
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
                .body(

                        customerService.getByCustomerId(
                                request.pathVariable("customerId")),
                        CustomerDTO.class);
    }
//
//    public Mono<ServerResponse> createNewCustomer(ServerRequest serverRequest) {
//        return customerService.saveCustomer(serverRequest.bodyToMono(CustomerDto.class)
//                .doOnNext(this::validate)
//                .flatMap(customerDto -> ServerResponse
//                        .created(UriComponentsBuilder
//                                .fromPath(CustomerRouterConfig.CUSTOMER_ID_PATH)
//                                .build(customerDto.getId()))
//                        .build());
//
//    }


    public Mono<ServerResponse> createNewCustomer(ServerRequest request){
        return customerService.saveNewCustomer(request.bodyToMono(CustomerDTO.class)
                        .doOnNext(this::validate))
                .flatMap(customerDTO -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(CustomerRouterConfig.CUSTOMER_PATH_ID)
                                .build(customerDTO.getId()))
                        .build());
    }

}
