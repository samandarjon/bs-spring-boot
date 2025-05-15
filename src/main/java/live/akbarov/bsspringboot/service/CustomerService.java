package live.akbarov.bsspringboot.service;

import live.akbarov.bsspringboot.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    Page<CustomerDTO> getCustomers(Pageable pageable);

    CustomerDTO getCustomer(Long id);

    CustomerDTO createCustomer(CustomerDTO.CreateCustomerDTO createCustomerDTO);

    CustomerDTO updateCustomer(Long id, CustomerDTO.UpdateCustomerDTO updateCustomerDTO);

    void deleteCustomer(Long id);

}
