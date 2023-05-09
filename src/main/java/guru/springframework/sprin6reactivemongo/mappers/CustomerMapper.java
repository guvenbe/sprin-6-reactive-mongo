package guru.springframework.sprin6reactivemongo.mappers;

import guru.springframework.sprin6reactivemongo.domain.Customer;
import guru.springframework.sprin6reactivemongo.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDto customerDto);
    CustomerDto customerToCustomerDto(Customer customer);
}
