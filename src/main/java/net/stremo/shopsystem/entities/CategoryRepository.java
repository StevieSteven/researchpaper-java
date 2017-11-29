package net.stremo.shopsystem.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository  extends PagingAndSortingRepository<Category, Long> {

    List<Category> findByName(String name);
}
