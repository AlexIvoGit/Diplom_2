import api.client.IngredientClient;
import api.client.OrderClient;
import api.client.UserClient;
import api.model.pojo.ingredient.Ingredient;
import api.model.pojo.order.SuccessResponseCreateOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Создание заказов")
public class CreateOrderTest {
    private IngredientClient ingredientClient = new IngredientClient();
    private UserClient userClient = new UserClient();
    private OrderClient orderClient = new OrderClient();
    private List<Ingredient> availableIngredients;
    private String accessToken = null;

    @Before
    public void setUp() {
        availableIngredients = ingredientClient.getAvailableIngredients();
        accessToken = userClient
                .createUser(userClient.getMapGeneratedDataUser())
                .then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа")
    public void createOrder() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(availableIngredients.get(0));
        ingredients.add(availableIngredients.get(1));

        SuccessResponseCreateOrder body = orderClient.createOrder(ingredients, accessToken)
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().as(SuccessResponseCreateOrder.class);

        Assert.assertTrue("Поле 'success' при авторизации не соответствует ожидаемому", body.success);
        Assert.assertNotNull("Название бургера равно NULL", body.name);

        ArrayList<Ingredient> actualIngredients = body.order.ingredients;

        assertThat(actualIngredients)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(ingredients)
                .as("Добавленные ингредиенты не соответствуют отправленным в заказ");
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthorization() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(availableIngredients.get(0));
        ingredients.add(availableIngredients.get(1));

        SuccessResponseCreateOrder body = orderClient.createOrder(ingredients, "")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().as(SuccessResponseCreateOrder.class);

        Assert.assertTrue("Поле 'success' при авторизации не соответствует ожидаемому", body.success);
        Assert.assertNotNull("Название бургера равно NULL", body.name);
    }

    @Test
    @DisplayName("Создание заказа без указания ингредиентов")
    public void createOrderWithoutIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        ResponseBodyExtractionOptions body = orderClient.createOrder(ingredients, accessToken)
                .then()
                .log().all()
                .statusCode(400)
                .extract().body();

        Assert.assertFalse("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение о не добавленных ингредиентах не соответствует ожидаемому"
                , "Ingredient ids must be provided", body.path("message"));
    }

    @Test
    @DisplayName("Создание заказа c указанием неверного хеша ингредиентов")
    public void createOrderWithNotValidHashIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(availableIngredients.get(0));
        ingredients.add(availableIngredients.get(1));

        ingredients.get(0).set_id(RandomStringUtils.randomAlphabetic(10));
        ingredients.get(1).set_id(RandomStringUtils.randomAlphabetic(10));

        orderClient.createOrder(ingredients, accessToken)
                .then()
                .log().all()
                .statusCode(500);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken)
                    .then()
                    .log().all();
            accessToken = null;
        }
    }
}
