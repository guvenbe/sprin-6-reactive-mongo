package guru.springframework.sprin6reactivemongo.repositories;

import guru.springframework.sprin6reactivemongo.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
