package drinkreview.api;

import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.delivery.DeliveryRepository;
import drinkreview.domain.delivery.dto.DeliveryResponseDto;
import drinkreview.global.enums.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryApiController {

    private final DeliveryRepository deliveryRepository;

    @GetMapping("/delivery")
    public Page<DeliveryResponseDto> deliveryPage(Pageable pageable) {
        Page<Delivery> page = deliveryRepository.findPage(pageable);
        return page.map(DeliveryResponseDto::new);
    }

    @GetMapping("/deliveries")
    public Page<DeliveryResponseDto> deliveryPageByStatus(@RequestParam("status") DeliveryStatus status, Pageable pageable) {
        Page<Delivery> page = deliveryRepository.findPageByStatus(status, pageable);
        return page.map(DeliveryResponseDto::new);
    }
}
