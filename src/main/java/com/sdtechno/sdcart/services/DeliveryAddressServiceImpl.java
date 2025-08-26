package com.sdtechno.sdcart.services;

import com.sdtechno.sdcart.models.DeliveryAddress;
import com.sdtechno.sdcart.models.User;
import com.sdtechno.sdcart.repositories.DeliveryAddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    private final DeliveryAddressRepository addressRepository;

    public DeliveryAddressServiceImpl(DeliveryAddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public DeliveryAddress addAddress(User user, DeliveryAddress address) {
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public List<DeliveryAddress> getUserAddresses(User user) {
        return addressRepository.findByUser(user);
    }

    @Override
    public DeliveryAddress updateAddress(Long id, DeliveryAddress newAddress, User user) {
        DeliveryAddress existing = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!existing.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized update");
        }

        existing.setFullName(newAddress.getFullName());
        existing.setMobileNumber(newAddress.getMobileNumber());
        existing.setAltMobileNumber(newAddress.getAltMobileNumber());
        existing.setAddressLine1(newAddress.getAddressLine1());
        existing.setAddressLine2(newAddress.getAddressLine2());
        existing.setCity(newAddress.getCity());
        existing.setState(newAddress.getState());
        existing.setPincode(newAddress.getPincode());
        existing.setLandmark(newAddress.getLandmark());

        return addressRepository.save(existing);
    }

    @Override
    public void deleteAddress(Long id, User user) {
        DeliveryAddress existing = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!existing.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized delete");
        }

        addressRepository.delete(existing);
    }
}
