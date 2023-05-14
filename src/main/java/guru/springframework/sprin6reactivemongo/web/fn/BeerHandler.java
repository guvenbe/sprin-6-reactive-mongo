package guru.springframework.sprin6reactivemongo.web.fn;

import guru.springframework.sprin6reactivemongo.model.BeerDTO;
import guru.springframework.sprin6reactivemongo.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;

    public Mono<ServerResponse> listBeers(ServerRequest serverRequest){
        return ServerResponse.ok()
                .body(beerService.listBeers(), BeerDTO.class);
    }
}
