package api.client;

import api.model.pojo.ingredient.Ingredient;
import api.model.pojo.ingredient.Ingredients;

import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientClient {
    private final String ENDPOINT = "https://stellarburgers.nomoreparties.site/api/ingredients";

    public List<Ingredient> getAvailableIngredients(){
        return given()
                .log().all()
                .get(ENDPOINT)
                .body().as(Ingredients.class)
                .getData();
    }
}
