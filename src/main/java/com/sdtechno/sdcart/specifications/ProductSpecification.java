package com.sdtechno.sdcart.specifications;

import com.sdtechno.sdcart.dto.SearchCriteria;
import com.sdtechno.sdcart.models.Product;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> byCriteria(SearchCriteria c) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔍 KEYWORD (name OR brand OR category)
            if (c.getKeyword() != null) {
                String like = "%" + c.getKeyword().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("name")), like),
                                cb.like(cb.lower(root.get("brand")), like),
                                cb.like(cb.lower(root.get("category")), like)
                        )
                );
            }

            // 📂 CATEGORY (optional strict)
            if (c.getCategory() != null) {
                predicates.add(
                        cb.equal(
                                cb.lower(root.get("category")),
                                c.getCategory().toLowerCase()
                        )
                );
            }

            // 💰 PRICE
            if (c.getMinPrice() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("price"), c.getMinPrice())
                );
            }

            if (c.getMaxPrice() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("price"), c.getMaxPrice())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
