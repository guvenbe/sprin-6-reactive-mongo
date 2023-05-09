package guru.springframework.sprin6reactivemongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {

    Integer id;
    String customerName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
