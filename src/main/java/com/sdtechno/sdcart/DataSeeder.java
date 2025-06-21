package com.sdtechno.sdcart;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.repositories.ProductRepository;

@Component
public class DataSeeder implements CommandLineRunner{

	@Autowired
	private ProductRepository productrepository;
	@Override
	public void run(String ...args) throws Exception{
		if(productrepository.count() == 0) {
			List<Product> products = Arrays.asList(
				 new Product("Realme narzo 70pro ",18000,"The realme narzo 70 pro is the best mobile",9.8,"Realme",10,120),
				 new Product("Realme 70pro ",25000,"The realme 70 pro is the best mobile",7.8,"Realme",10,120)
			);
			productrepository.saveAll(products);
			System.out.println("Data seeded successfully....(*_*)");
		}
	}
}
