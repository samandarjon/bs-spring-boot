package live.akbarov.bsspringboot.service.impl;

import live.akbarov.bsspringboot.dto.CustomerDTO;
import live.akbarov.bsspringboot.exception.GeneralException;
import live.akbarov.bsspringboot.exception.NotFoundException;
import live.akbarov.bsspringboot.mapper.CustomerMapper;
import live.akbarov.bsspringboot.repo.CustomerRepository;
import live.akbarov.bsspringboot.service.CustomerService;
import live.akbarov.bsspringboot.until.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Page<CustomerDTO> getCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(CustomerMapper.INSTANCE::toCustomerDTO);
    }

    @Override
    public CustomerDTO getCustomer(Long id) {
        return customerRepository.findById(id).map(CustomerMapper.INSTANCE::toCustomerDTO)
                .orElseThrow(() -> NotFoundException.create("Customer with id " + id + " not found"));
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO.CreateCustomerDTO customerDTO) {
        ValidationUtil.onCondition(customerRepository.existsByEmail(customerDTO.getEmail()),
                "Email is already exist");
        return Optional.of(customerDTO)
                .map(CustomerMapper::toCustomerEntity)
                .map(customerRepository::save)
                .map(CustomerMapper.INSTANCE::toCustomerDTO)
                .orElseThrow(() -> GeneralException.create("Customer can not be null"));
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO.UpdateCustomerDTO customerDTO) {
        ValidationUtil.onCondition(customerRepository.existsByEmailAndIdNot(customerDTO.getEmail(), id),
                "Email is already exist");
        return customerRepository.findById(id)
                .map(entity -> CustomerMapper.toUpdatedCustomerDTO(entity, customerDTO))
                .map(customerRepository::save)
                .map(CustomerMapper.INSTANCE::toCustomerDTO)
                .orElseThrow(() -> NotFoundException.create("Customer with id " + id + " not found"));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
