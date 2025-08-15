package com.sdtechno.sdcart.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sdtechno.sdcart.models.Advertisement;
import com.sdtechno.sdcart.repositories.AdvertisementRepository;

@RestController
@RequestMapping("/api/ads")
public class AdvertisementController {

    @Autowired
    private AdvertisementRepository adRepo;

    @Autowired
    private Cloudinary cloudinary;

    // Upload a new ad
    @PostMapping("/upload")
    public ResponseEntity<?> uploadAd(@RequestParam("image") MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            
            Advertisement ad = new Advertisement();
            ad.setImageUrl(uploadResult.get("secure_url").toString());
            ad.setPublicId(uploadResult.get("public_id").toString());
            
            adRepo.save(ad);
            return ResponseEntity.ok(ad);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload ad: " + e.getMessage());
        }
    }

    // Get all ads (newest first)
    @GetMapping
    public ResponseEntity<List<Advertisement>> getAllAds() {
        List<Advertisement> ads = adRepo.findAll()
                                         .stream()
                                         .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                                         .collect(Collectors.toList());
        return ResponseEntity.ok(ads);
    }

    // Delete an ad by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id) {
        Advertisement ad = adRepo.findById(id).orElse(null);
        if (ad == null) return ResponseEntity.status(404).body("Ad not found");

        try {
            cloudinary.uploader().destroy(ad.getPublicId(), ObjectUtils.emptyMap());
            adRepo.delete(ad);
            return ResponseEntity.ok("Ad deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete ad: " + e.getMessage());
        }
    }
}
