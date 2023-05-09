package guru.springframework.sprin6reactivemongo.mappers;

import guru.springframework.sprin6reactivemongo.domain.Beer;
import guru.springframework.sprin6reactivemongo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerDTO beerToBeerDto(Beer beer);
    Beer beerdtoToBeer(BeerDTO beerDTO);
}
