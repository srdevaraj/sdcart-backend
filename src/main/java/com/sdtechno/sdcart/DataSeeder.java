package com.sdtechno.sdcart;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.repositories.ProductRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        long count = productRepository.count();

        if (count == 0) {
            List<Product> products = Arrays.asList(
                new Product(
                    "Realme Narzo 70 Pro",
                    18000.0,
                    "The Realme Narzo 70 Pro is the best budget mobile",
                    9.8,
                    "Realme",
                    10,
                    120,
                    "Smartphones",   // category
                    "Realme"        // brand
                ),
                new Product(
                    "Realme 70 Pro",
                    25000.0,
                    "The Realme 70 Pro is a high-performance smartphone",
                    8.5,
                    "Realme",
                    15,
                    100,
                    "Smartphones",   // category
                    "Realme"        // brand
                ),
                new Product(
                    "Samsung Galaxy F55",
                    17999.0,
                    "The Samsung Galaxy F55 5G is a mid-range smartphone with a vegan leather design",
                    9.0,
                    "Samsung",
                    20,
                    95,
                    "Smartphones",   // category
                    "Samsung"       // brand
                ),
                new Product(
                    "iPhone 15",
                    79999.0,
                    "The Apple iPhone 15 with A16 Bionic chip and Dynamic Island",
                    9.5,
                    "Apple",
                    5,
                    200,
                    "Smartphones",   // category
                    "Apple"         // brand
                )
            );

            productRepository.saveAll(products);
            System.out.println("✅ Data seeded successfully with " + products.size() + " products.");
        } else {
            System.out.println("ℹ️ Skipping seeding. Already found " + count + " products in DB.");
        }
    }
}
