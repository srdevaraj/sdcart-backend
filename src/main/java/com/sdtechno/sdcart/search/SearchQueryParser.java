package com.sdtechno.sdcart.search;

import com.sdtechno.sdcart.dto.SearchCriteria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchQueryParser {

    public SearchCriteria parse(String query) {

        SearchCriteria c = new SearchCriteria();
        String[] tokens = query.toLowerCase().split("\\s+");

        for (int i = 0; i < tokens.length; i++) {

            String token = tokens[i];

            // CATEGORY
            if (List.of("mobile", "grocery", "fruit", "electrical").contains(token)) {
                c.setCategory(token);
                continue;
            }

            // PRICE
            if (token.equals("under") && i + 1 < tokens.length) {
                c.setMaxPrice(Double.parseDouble(tokens[i + 1]));
                continue;
            }

            // MOBILE attributes
            if (token.endsWith("gb")) {
                c.getAttributes().put("memory", token);
                continue;
            }

            // GROCERY / FRUIT attributes
            if (token.endsWith("kg")) {
                c.getAttributes().put("weight", token);
                continue;
            }

            // ORGANIC
            if (token.equals("organic")) {
                c.getAttributes().put("organic", "true");
                continue;
            }

            // KEYWORD (fallback)
            if (c.getKeyword() == null) {
                c.setKeyword(token);
            }
        }

        return c;
    }
}
