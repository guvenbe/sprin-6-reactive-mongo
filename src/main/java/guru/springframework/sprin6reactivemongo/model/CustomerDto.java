package guru.springframework.sprin6reactivemongo.model;

import jakarta.validation.constraints.NotBlank;
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

    String id;

    @NotBlank
    String customerName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
