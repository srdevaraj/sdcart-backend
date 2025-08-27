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
        // Check if user already has an address
        DeliveryAddress existing = addressRepository.findByUser(user).orElse(null);

        if (existing != null) {
            // Update existing address fields
            existing.setFullName(address.getFullName());
            existing.setMobileNumber(address.getMobileNumber());
            existing.setAltMobileNumber(address.getAltMobileNumber());
            existing.setAddressLine1(address.getAddressLine1());
            existing.setAddressLine2(address.getAddressLine2());
            existing.setCity(address.getCity());
            existing.setState(address.getState());
            existing.setPincode(address.getPincode());
            existing.setLandmark(address.getLandmark());
            return addressRepository.save(existing);
        } else {
            // No address yet → create new
            address.setUser(user);
            return addressRepository.save(address);
        }
    }


    @Override
    public DeliveryAddress getUserAddress(User user) {
        return addressRepository.findByUser(user).orElse(null);
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
