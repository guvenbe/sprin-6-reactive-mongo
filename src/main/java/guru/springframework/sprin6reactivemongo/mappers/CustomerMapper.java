package guru.springframework.sprin6reactivemongo.mappers;

import guru.springframework.sprin6reactivemongo.domain.Customer;
import guru.springframework.sprin6reactivemongo.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO customerDto);
    CustomerDTO customerToCustomerDto(Customer customer);
}
