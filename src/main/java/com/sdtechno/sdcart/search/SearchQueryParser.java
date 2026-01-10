package com.sdtechno.sdcart.search;

import com.sdtechno.sdcart.dto.SearchCriteria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchQueryParser {

    private static final List<String> CATEGORIES =
            List.of("mobile", "grocery", "fruit", "electrical");

    private static final List<String> BRANDS =
            List.of("redmi", "realme", "samsung", "apple", "vivo");

    public SearchCriteria parse(String query) {

        SearchCriteria c = new SearchCriteria();
        String[] tokens = query.toLowerCase().split("\\s+");

        StringBuilder keywordBuilder = new StringBuilder();

        for (int i = 0; i < tokens.length; i++) {

            String token = tokens[i];

            // CATEGORY
            if (CATEGORIES.contains(token)) {
                c.setCategory(token);
                continue;
            }

            // BRAND
            if (BRANDS.contains(token)) {
                c.setBrand(token);
                keywordBuilder.append(token).append(" "); // ⭐ keep keyword
                continue;
            }

            // PRICE UNDER
            if (token.equals("under") && i + 1 < tokens.length) {
                try {
                    c.setMaxPrice(Double.parseDouble(tokens[i + 1]));
                } catch (Exception ignored) {}
                i++; // skip price number
                continue;
            }

            // PRICE ABOVE
            if (token.equals("above") && i + 1 < tokens.length) {
                try {
                    c.setMinPrice(Double.parseDouble(tokens[i + 1]));
                } catch (Exception ignored) {}
                i++;
                continue;
            }

            // MEMORY (future)
            if (token.endsWith("gb")) {
                continue;
            }

            // NORMAL KEYWORD
            keywordBuilder.append(token).append(" ");
        }

        String keyword = keywordBuilder.toString().trim();
        if (!keyword.isEmpty()) {
            c.setKeyword(keyword);
        }

        return c;
    }
}

