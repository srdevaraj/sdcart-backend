package com.sdtechno.sdcart.services;

import com.sdtechno.sdcart.models.DeliveryAddress;
import com.sdtechno.sdcart.models.User;

public interface DeliveryAddressService {
    // Add a new address or update the existing one
    DeliveryAddress addAddress(User user, DeliveryAddress address);

    // Get the single address for a user
    DeliveryAddress getUserAddress(User user);

    // Update address by id
    DeliveryAddress updateAddress(Long id, DeliveryAddress address, User user);

    // Delete address by id
    void deleteAddress(Long id, User user);
}
