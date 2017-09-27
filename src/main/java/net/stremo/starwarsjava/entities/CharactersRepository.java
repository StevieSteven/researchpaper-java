package net.stremo.starwarsjava.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CharactersRepository extends PagingAndSortingRepository<Character, Long> {

}
