package com.sdtechno.sdcart.repositories;

import com.sdtechno.sdcart.models.DeliveryAddress;
import com.sdtechno.sdcart.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    Optional<DeliveryAddress> findByUser(User user);
}
