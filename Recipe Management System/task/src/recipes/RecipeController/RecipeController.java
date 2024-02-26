package recipes.RecipeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import recipes.Entity.RecipeEntity;
import recipes.Exceptions.BadRequestException;
import recipes.Exceptions.RecipeNotFoundException;
import recipes.Models.CreateRecipeDTO;
import recipes.Models.RecipeDTO;
import recipes.Models.RecipeID;
import recipes.Models.UserDto;
import recipes.Service.RecipeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recipe/")
public class RecipeController {
    private RecipeEntity recipeEntity;

    @Autowired
    private RecipeService recipeService;


    @GetMapping("{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable("id") int id)  {
        RecipeDTO recipeDTO = recipeService.getRecipeByID(id);
        return new ResponseEntity<>(recipeDTO, HttpStatus.OK);
    }

    @GetMapping("search/")
    public ResponseEntity<List<RecipeDTO>> getRecipeBySearch(
            @RequestParam(name = "category", required = false) String paramValue1,
            @RequestParam(name = "name", required = false) String paramValue2
    ) {
        String paramKey = paramValue1 == null ? (paramValue2 == null ? null: "name" ): "category";
        List<RecipeDTO> recipeDTOs = recipeService.getBySearch( paramKey, paramValue1 == null ? paramValue2: paramValue1);
        return new ResponseEntity<>(recipeDTOs,HttpStatus.OK);
    }

    @PostMapping("new")
    public ResponseEntity<RecipeID> addRecipe(
            @Valid @RequestBody CreateRecipeDTO createRecipeDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        RecipeID recipeId = recipeService.addRecipe(createRecipeDTO, userDetails.getPassword());
        return new ResponseEntity<>(recipeId, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRecipe(
            @PathVariable("id") int id,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        UserDto recipeAuthor = recipeService.getRecipeAuthor(id);

        if(isUseAuthorised(userDetails, recipeAuthor)){
            recipeService.deleteRecipeById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecipe(
            @Valid @RequestBody CreateRecipeDTO createRecipeDTO,
            @PathVariable("id") int id,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        UserDto recipeAuthor = recipeService.getRecipeAuthor(id);

        if(isUseAuthorised(userDetails, recipeAuthor)){
            recipeService.updateRecipe(id, createRecipeDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private boolean isUseAuthorised(UserDetails userDetails, UserDto userDto){
        return userDetails != null && userDetails.getPassword().equals(userDto.getPassword());
    }


    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleWrongTokenException(RecipeNotFoundException recipeNotFoundException) {
        return new ResponseEntity<>(recipeNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> handleBadRequestExceptions() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}



