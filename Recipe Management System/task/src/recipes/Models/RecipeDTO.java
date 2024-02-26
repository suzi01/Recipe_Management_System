package recipes.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipes.Entity.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {

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

    @NotBlank
    @Size(min = 8, message = "Date must have a size greater than 8")
    private LocalDateTime date;

    @JsonIgnore
    private User user;
}
