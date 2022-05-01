package drinkreview.domain.drink;

import drinkreview.domain.drink.dto.DrinkRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    public void saveImageFile(Long drinkId, MultipartFile file) {
        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new IllegalStateException("Drink is null."));

        try {
            Byte[] byteObjects = new Byte[file.getBytes().length];
            int i = 0;

            for (Byte b : byteObjects) {
                byteObjects[i++] = b;
            }

            drink.updateImage(byteObjects);
            drinkRepository.save(drink);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public Drink findDrink(Long drinkId) {
        return drinkRepository.findById(drinkId)
                .orElseThrow(() -> new IllegalStateException("Drink is null."));
    }

    @Transactional(readOnly = true)
    public List<Drink> findDrinks() {
        return drinkRepository.findAll();
    }
}
