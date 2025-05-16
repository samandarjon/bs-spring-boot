package live.akbarov.bsspringboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CustomerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateCustomerDTO {
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        @NotBlank
        @Email
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateCustomerDTO {
        private String firstName;
        private String lastName;
        @Email
        private String email;
    }
}
