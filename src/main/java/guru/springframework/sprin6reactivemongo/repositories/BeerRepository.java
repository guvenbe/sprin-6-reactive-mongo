package guru.springframework.sprin6reactivemongo.repositories;

import guru.springframework.sprin6reactivemongo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {
}
