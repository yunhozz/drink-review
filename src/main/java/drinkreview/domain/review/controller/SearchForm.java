package drinkreview.domain.review.controller;

import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class SearchForm {

    private String keyword;

    @Enumerated(EnumType.STRING)
    private OrderSelect orderSelect; //DATE_ORDER, ACCURACY_ORDER
}
