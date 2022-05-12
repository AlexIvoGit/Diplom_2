package api.client;

import api.model.pojo.ingredient.Ingredient;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private final String JSON = "application/json";
    private final String ENDPOINT = "https://stellarburgers.nomoreparties.site/api/orders";

    @Step("Создание заказа")
    public Response createOrder(List<Ingredient> ingredients, String accessToken) {
        Map<String, String[]> body = new HashMap<>();
        String[] arrayIngredient = new String[ingredients.size()];

        for (int i = 0; i < ingredients.size(); i++) {
            arrayIngredient[i] = ingredients.get(i).get_id();
        }

        body.put("ingredients", arrayIngredient);

        return given()
                .log().all()
                .header("Content-type", JSON)
                .header("Authorization", accessToken)
                .and()
                .body(body)
                .post(ENDPOINT);
    }

    @Step("Получение заказов")
    public Response getAllOrders(String accessToken){
        return given()
                .log().all()
                .header("Authorization", accessToken)
                .get(ENDPOINT);
    }
}
