package net.stremo.shopsystem.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository  extends PagingAndSortingRepository<Product, Long> {
}
