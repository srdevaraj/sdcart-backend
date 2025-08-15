package com.sdtechno.sdcart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sdtechno.sdcart.models.Advertisement;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}
