package recipes.RecipeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import recipes.Entity.User;
import recipes.Exceptions.UserAlreadyExistsException;
import recipes.Models.UserDto;
import recipes.Service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {
    private User user;

    @Autowired
    private UserService userService;


    @PostMapping("register")
    public ResponseEntity<Void> addRecipe(@Valid @RequestBody UserDto userDto) {
        userService.verifyUser(userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> handleUserAlreadyExistsException() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}