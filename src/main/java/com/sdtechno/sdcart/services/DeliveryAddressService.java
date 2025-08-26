package com.sdtechno.sdcart.services;

import com.sdtechno.sdcart.models.DeliveryAddress;
import com.sdtechno.sdcart.models.User;

import java.util.List;

public interface DeliveryAddressService {
    DeliveryAddress addAddress(User user, DeliveryAddress address);
    List<DeliveryAddress> getUserAddresses(User user);
    DeliveryAddress updateAddress(Long id, DeliveryAddress address, User user);
    void deleteAddress(Long id, User user);
}
