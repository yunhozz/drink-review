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

    public void uploadImageFile(Long drinkId, MultipartFile file) {
        Drink drink = this.findDrink(drinkId);

        try {
            Byte[] byteObjects = new Byte[file.getBytes().length];
            int i = 0;

            for (Byte b : byteObjects) {
                byteObjects[i++] = b;
            }

            drink.updateImage(byteObjects);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public DrinkResponseDto findDrinkDto(Long drinkId) {
        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new IllegalStateException("Drink is null."));

        return new DrinkResponseDto(drink);
    }

    @Transactional(readOnly = true)
    private Drink findDrink(Long drinkId) {
        return drinkRepository.findById(drinkId)
                .orElseThrow(() -> new IllegalStateException("Drink is null."));
    }
}
