package live.akbarov.bsspringboot.repo;

import live.akbarov.bsspringboot.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
