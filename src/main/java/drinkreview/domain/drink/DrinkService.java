package drinkreview.domain.drink;

import drinkreview.domain.drink.dto.DrinkRequestDto;
import drinkreview.domain.drink.dto.DrinkResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class DrinkService {

    private final DrinkRepository drinkRepository;

    public Long saveDrink(DrinkRequestDto drinkRequestDto) {
        Drink drink = drinkRequestDto.toEntity();
        drinkRepository.save(drink);

        return drink.getId();
    }

    public void uploadImageFile(Long drinkId, MultipartFile file) throws Exception {
        Drink drink = this.findDrink(drinkId);
        drink.updateImage(file);
    }

    public void deleteDrink(Long drinkId) {
        Drink drink = this.findDrink(drinkId);
        drinkRepository.delete(drink);
    }

    @Transactional(readOnly = true)
    public DrinkResponseDto findDrinkDto(Long drinkId) {
        Drink drink = this.findDrink(drinkId);
        return new DrinkResponseDto(drink);
    }

    @Transactional(readOnly = true)
    private Drink findDrink(Long drinkId) {
        return drinkRepository.findById(drinkId)
                .orElseThrow(() -> new IllegalStateException("Drink is null."));
    }
}
