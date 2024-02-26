package recipes.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    private String email;
    private String username;
    private String password;
    private String authority;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RecipeEntity> recipes;

}
