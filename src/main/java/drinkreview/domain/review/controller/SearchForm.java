package drinkreview.domain.review.controller;

import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SearchForm {

    @NotBlank(message = "Please input keyword.")
    private String keyword;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderSelect orderSelect; //DATE_ORDER, ACCURACY_ORDER
}
