package project_testing.demo.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    List<Item> findByCategoriesContainingIgnoreCase(String category);

    List<Item> findByNameContainingIgnoreCase(String name);

    List<Item> findByBrandNameIgnoreCase(String brandName);

    List<Item> findByBrandId(Long brandId);
}