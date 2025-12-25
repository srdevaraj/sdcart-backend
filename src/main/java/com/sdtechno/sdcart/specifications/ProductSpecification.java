package com.sdtechno.sdcart.specifications;

import com.sdtechno.sdcart.dto.SearchCriteria;
import com.sdtechno.sdcart.models.Product;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductSpecification {

    public static Specification<Product> byCriteria(SearchCriteria criteria) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // =========================
            // KEYWORD SEARCH (name + description)
            // =========================
            if (criteria.getKeyword() != null) {
                String likePattern = "%" + criteria.getKeyword().toLowerCase() + "%";

                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("name")), likePattern),
                        cb.like(cb.lower(root.get("description")), likePattern)
                    )
                );
            }

            // =========================
            // CATEGORY FILTER
            // =========================
            if (criteria.getCategory() != null) {
                predicates.add(
                    cb.equal(
                        cb.lower(root.get("category")),
                        criteria.getCategory().toLowerCase()
                    )
                );
            }

            // =========================
            // PRICE FILTERS
            // =========================
            if (criteria.getMinPrice() != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(
                        root.get("price"),
                        criteria.getMinPrice()
                    )
                );
            }

            if (criteria.getMaxPrice() != null) {
                predicates.add(
                    cb.lessThanOrEqualTo(
                        root.get("price"),
                        criteria.getMaxPrice()
                    )
                );
            }

            // =========================
            // 🔥 ATTRIBUTE FILTERS (RAM, STORAGE, KG, ORGANIC, etc.)
            // =========================
            if (!criteria.getAttributes().isEmpty()) {

                MapJoin<Product, String, String> attrJoin =
                        root.joinMap("attributes");

                for (Map.Entry<String, String> entry : criteria.getAttributes().entrySet()) {

                    Predicate keyMatch =
                            cb.equal(attrJoin.key(), entry.getKey());

                    Predicate valueMatch =
                            cb.equal(attrJoin.value(), entry.getValue());

                    predicates.add(cb.and(keyMatch, valueMatch));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
