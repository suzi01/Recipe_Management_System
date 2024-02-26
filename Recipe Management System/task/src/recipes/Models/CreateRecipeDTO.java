package recipes.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecipeDTO {

    @NotBlank
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Description cannot be blank")
    private String category;

    @NotNull
    @Size(min=2, message = "Ingredients must have a size greater than 1")
    private List<String> ingredients;

    @NotNull
    @Size(min=2, message = "Ingredients must have a size greater than 1")
    private List<String> directions;
}
