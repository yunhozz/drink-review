package drinkreview.domain.drink;

import drinkreview.domain.drink.controller.DrinkForm;
import drinkreview.domain.drink.dto.DrinkResponseDto;
import drinkreview.domain.drink.dto.DrinkSimpleResponseDto;
import drinkreview.domain.drink.repository.DrinkRepository;
import drinkreview.global.enums.DrinkStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DrinkService {

    private final DrinkRepository drinkRepository;

    public Long saveDrink(DrinkForm form) {
        Drink drink = Drink.builder()
                .name(form.getName())
                .country(form.getCountry())
                .productionDate(LocalDate.of(form.getYear(), form.getMonth(), form.getDay()))
                .price(form.getPrice())
                .stockQuantity(form.getStockQuantity())
                .status(DrinkStatus.ON_SALE)
                .build();

        drinkRepository.save(drink);
        return drink.getId();
    }

    public void uploadImageFile(Long drinkId, MultipartFile file) throws Exception {
        Drink drink = this.findDrink(drinkId);
        drink.updateImage(file);
    }

    @Transactional(readOnly = true)
    public DrinkResponseDto findDrinkDto(Long drinkId) {
        Drink drink = this.findDrink(drinkId);
        return new DrinkResponseDto(drink);
    }

    @Transactional(readOnly = true)
    public List<DrinkSimpleResponseDto> findDrinkSimpleDtoList() {
        List<DrinkSimpleResponseDto> dtoList = new ArrayList<>();
        List<Drink> drinks = drinkRepository.findAll();

        for (Drink drink : drinks) {
            dtoList.add(new DrinkSimpleResponseDto(drink));
        }

        return dtoList;
    }

    @Transactional(readOnly = true)
    private Drink findDrink(Long drinkId) {
        return drinkRepository.findById(drinkId)
                .orElseThrow(() -> new IllegalStateException("Drink is null."));
    }
}
