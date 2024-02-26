package recipes.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.Entity.RecipeEntity;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<RecipeEntity, Integer> {
    List<RecipeEntity> findByCategoryIgnoreCaseOrderByDateDesc(String category);
    List<RecipeEntity> findAllByNameContainsIgnoreCaseOrderByDateDesc(String name);
}

