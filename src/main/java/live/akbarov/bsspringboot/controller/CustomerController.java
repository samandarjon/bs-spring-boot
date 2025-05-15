package live.akbarov.bsspringboot.controller;

import live.akbarov.bsspringboot.dto.CustomerDTO;
import live.akbarov.bsspringboot.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        return customerService.getCustomers(pageable);
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public CustomerDTO createCustomer(@Validated @RequestBody CustomerDTO.CreateCustomerDTO customerDTO) {
        return customerService.createCustomer(customerDTO);
    }

    @PatchMapping("/{id}")
    public CustomerDTO patchCustomer(@PathVariable Long id,
                                     @Validated @RequestBody CustomerDTO.UpdateCustomerDTO customerDTO) {
        return customerService.updateCustomer(id, customerDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
}
