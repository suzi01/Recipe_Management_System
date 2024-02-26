package recipes.Service.Impl;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.Entity.RecipeEntity;
import recipes.Entity.User;
import recipes.Exceptions.BadRequestException;
import recipes.Exceptions.RecipeNotFoundException;
import recipes.Models.CreateRecipeDTO;
import recipes.Models.RecipeDTO;
import recipes.Models.RecipeID;
import recipes.Models.UserDto;
import recipes.Repository.RecipeRepository;
import recipes.Repository.UserRepository;
import recipes.Service.RecipeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final ModelMapper modelMapper;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;



    public RecipeServiceImpl(){
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setCollectionsMergeEnabled(false); // Ensures strict field matching
    }

    @Override
    public RecipeDTO getRecipeByID(int id) {
        Optional<RecipeEntity> optionalRecipe = recipeRepository.findById(id);
        RecipeEntity recipeEntity = new RecipeEntity();
        if(optionalRecipe.isPresent()){
            return modelMapper.map(optionalRecipe, RecipeDTO.class);
        }
        throw new RecipeNotFoundException("Not found");
    }

    @Override
    public UserDto getRecipeAuthor(int id) {
        Optional<RecipeEntity> optionalRecipe = recipeRepository.findById(id);
        RecipeEntity recipeEntity = new RecipeEntity();
        if(optionalRecipe.isPresent()){
            int userId = optionalRecipe.get().getUser().getId();
            return modelMapper.map(userRepository.findById(userId), UserDto.class);
        }
        throw new RecipeNotFoundException("Not found");
    }

    @Override
    public RecipeID addRecipe(CreateRecipeDTO createRecipeDTO, String userPassword) {
        Optional<User> user = userRepository.findUserByPassword(userPassword);
        RecipeEntity recipeEntity = modelMapper.map(createRecipeDTO, RecipeEntity.class);
        recipeEntity.setDate(LocalDateTime.now());

        if(user.isPresent()){
//            User newUser = recipeEntity.getUser();
//            newUser.setId(user.get().getId());
            User newUser = new User();
            newUser.setId(user.get().getId());
            recipeEntity.setUser(newUser);

        }

//        user.ifPresent(value -> recipeEntity.getUser().setId(value.getId()));
        recipeRepository.save(recipeEntity);
        return new RecipeID(recipeEntity.getId());
    }

    @Override
    public void deleteRecipeById(int id) {
        Optional<RecipeEntity> optionalRecipe = recipeRepository.findById(id);
        if(optionalRecipe.isPresent()){
            recipeRepository.deleteById(id);
        } else {
            throw new RecipeNotFoundException("Not found");
        }
    }

    @Override
    public void updateRecipe(int recipeId, CreateRecipeDTO createRecipeDTO) {
        Optional<RecipeEntity> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isPresent()){
            RecipeEntity te = optionalRecipe.get();
            modelMapper.map(createRecipeDTO, te);
            te.setDate(LocalDateTime.now());
            recipeRepository.save(te);
        } else {
            throw new RecipeNotFoundException("Not found");
        }
    }

    @Override
    public List<RecipeDTO> getBySearch(String paramName, String paramValue) {
        List<RecipeDTO> recipeDTOList = new ArrayList<>();
        if (paramName == null && paramValue == null) {

            throw new BadRequestException();
        }
        if(paramName.equals("category") && !paramValue.isEmpty() && !paramValue.contains(" ")){
            List<RecipeEntity> recipeEntities = recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(paramValue);
            for(RecipeEntity recipeEntity: recipeEntities) {
                RecipeDTO recipeDTO = modelMapper.map(recipeEntity, RecipeDTO.class);
                recipeDTOList.add(recipeDTO);
            }
            return recipeDTOList;
        }

        if(paramName.equals("name") && !paramValue.isEmpty() && !paramValue.contains(" ")){
            List<RecipeEntity> recipeEntities = recipeRepository.findAllByNameContainsIgnoreCaseOrderByDateDesc(paramValue);
            for(RecipeEntity recipeEntity: recipeEntities) {
                RecipeDTO recipeDTO = modelMapper.map(recipeEntity, RecipeDTO.class);
                recipeDTOList.add(recipeDTO);
            }
            return recipeDTOList;
        }


        throw new RecipeNotFoundException("");
    }
}
