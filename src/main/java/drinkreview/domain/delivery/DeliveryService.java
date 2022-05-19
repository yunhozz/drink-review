package drinkreview.domain.delivery;

import drinkreview.domain.delivery.dto.DeliveryRequestDto;
import drinkreview.domain.delivery.dto.DeliveryResponseDto;
import drinkreview.domain.order.Order;
import drinkreview.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    public Long makeDelivery(DeliveryRequestDto dto, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order is null."));

        dto.setOrder(order);
        Delivery delivery = deliveryRepository.save(dto.toEntity());

        return delivery.getId();
    }

    public void startDelivery(Long deliveryId) {
        Delivery delivery = this.findDelivery(deliveryId);
        delivery.start();
    }

    public void completeDelivery(Long deliveryId) {
        Delivery delivery = this.findDelivery(deliveryId);
        delivery.complete();
    }

    @Transactional(readOnly = true)
    public DeliveryResponseDto findDeliveryDto(Long deliveryId) {
        Delivery delivery = this.findDelivery(deliveryId);
        return new DeliveryResponseDto(delivery);
    }

    @Transactional(readOnly = true)
    public Delivery findDelivery(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalStateException("Delivery is null."));
    }
}
