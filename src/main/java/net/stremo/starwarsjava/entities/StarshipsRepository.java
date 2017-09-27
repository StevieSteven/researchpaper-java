package net.stremo.starwarsjava.entities;

import org.springframework.data.repository.PagingAndSortingRepository;
import net.stremo.starwarsjava.entities.Starship;

public interface StarshipsRepository extends PagingAndSortingRepository<Starship, Long> {

}
