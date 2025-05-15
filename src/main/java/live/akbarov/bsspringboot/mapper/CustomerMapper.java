package live.akbarov.bsspringboot.mapper;

import ch.qos.logback.core.util.StringUtil;
import live.akbarov.bsspringboot.dto.CustomerDTO;
import live.akbarov.bsspringboot.entity.CustomerEntity;
import org.springframework.util.StringUtils;

import java.util.Optional;

public interface CustomerMapper {
    CustomerMapper INSTANCE = new CustomerMapper() {
    };

    static CustomerEntity toUpdatedCustomerDTO(CustomerEntity entity,
                                               CustomerDTO.UpdateCustomerDTO customerDTO) {
        Optional.ofNullable(customerDTO.getEmail()).filter(StringUtils::hasText).ifPresent(entity::setEmail);
        Optional.ofNullable(customerDTO.getFirstName()).filter(StringUtils::hasText).ifPresent(entity::setFirstName);
        Optional.ofNullable(customerDTO.getLastName()).filter(StringUtils::hasText).ifPresent(entity::setLastName);
        return entity;
    }

    default CustomerDTO toCustomerDTO(CustomerEntity entity) {
        return new CustomerDTO()
                .setId(entity.getId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setEmail(entity.getEmail());
    }

    static CustomerEntity toCustomerEntity(CustomerDTO.CreateCustomerDTO dto) {
        return new CustomerEntity()
                .setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setEmail(dto.getEmail());
    }
}
