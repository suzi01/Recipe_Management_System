package recipes.Service;

import org.springframework.stereotype.Service;
import recipes.Models.CreateRecipeDTO;
import recipes.Models.RecipeDTO;
import recipes.Models.RecipeID;
import recipes.Models.UserDto;

import java.util.List;

@Service
public interface RecipeService {
    RecipeDTO getRecipeByID(int id);

    UserDto getRecipeAuthor(int id);

    RecipeID addRecipe(CreateRecipeDTO createRecipeDTO, String userPassword);

    void deleteRecipeById(int id);

    void updateRecipe(int recipeId, CreateRecipeDTO createRecipeDTO);

    List<RecipeDTO>getBySearch(String paramName, String paramValue);
}
