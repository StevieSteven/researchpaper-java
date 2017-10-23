package net.stremo.shopsystem.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderItemRepository  extends PagingAndSortingRepository<OrderItem, Long> {
}
