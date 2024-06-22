package guru.springframework.sprin6reactivemongo.web.fn;

import guru.springframework.sprin6reactivemongo.model.BeerDTO;
import guru.springframework.sprin6reactivemongo.repositories.BeerRepository;
import guru.springframework.sprin6reactivemongo.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;
    private final Validator validator;

    private void validate(BeerDTO beerDTO){
        Errors errors = new BeanPropertyBindingResult(beerDTO, "beerDTO");
        validator.validate(beerDTO, errors);
    }
    public Mono<ServerResponse> deleteBeerById(ServerRequest request) {
        return beerService.getById(request.pathVariable("beerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(beerDTO -> beerService.deleteBeerById(beerDTO.getId()))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchBeerById(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
                .flatMap(beerDTO -> beerService.patchBeer(request.pathVariable("beerId"), beerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateBeerById(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BeerDTO.class)
                .flatMap(beerDTO -> beerService.updateBeer(serverRequest.pathVariable("beerId"), beerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());

    }

    public Mono<ServerResponse> createNewBeer(ServerRequest serverRequest) {
        return beerService.saveBeer(serverRequest.bodyToMono(BeerDTO.class))
                .flatMap(beerDTO -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(BeerRouterConfig.BEER_PATH_ID)
                                .build(beerDTO.getId()))
                        .build());
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(beerService.getById(request.pathVariable("beerId"))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),
                        BeerDTO.class);
    }

    public Mono<ServerResponse> listBeers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(beerService.listBeers(), BeerDTO.class);
    }
}
