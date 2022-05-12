import api.client.IngredientClient;
import api.client.OrderClient;
import api.client.UserClient;
import api.model.pojo.ingredient.Ingredient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Получение заказов")
public class ReceiveOrdersTest {
    private IngredientClient ingredientClient = new IngredientClient();
    private UserClient userClient = new UserClient();
    private OrderClient orderClient = new OrderClient();
    private List<Ingredient> availableIngredients;
    private List<Ingredient> expectedIngredient = new ArrayList<>();
    private String accessToken = null;

    @Before
    public void setUp() {
        availableIngredients = ingredientClient.getAvailableIngredients();
        accessToken = userClient
                .createUser(userClient.getMapGeneratedDataUser())
                .then().extract().body().path("accessToken");

        expectedIngredient.add(availableIngredients.get(0));
        expectedIngredient.add(availableIngredients.get(1));

        orderClient.createOrder(expectedIngredient, accessToken);
    }

    @Test
    @DisplayName("Получение заказа конкретного пользователя")
    public void getAllOrdersSpecificUser(){
        ResponseBodyExtractionOptions body = orderClient
                .getAllOrders(accessToken)
                .then()
                .log().all()
                .statusCode(200)
                .extract().body();

        Assert.assertTrue("Поле 'success' не соответствует ожидаемому", body.path("success"));
        Assert.assertNotNull("Заказы равны NULL", body.path("orders"));
    }

    @Test
    @DisplayName("Получение заказа конкретного пользователя без авторизации")
    public void getAllOrdersSpecificUserWithoutAuthorization(){
        ResponseBodyExtractionOptions body = orderClient
                .getAllOrders("")
                .then()
                .log().all()
                .statusCode(401)
                .extract().body();

        Assert.assertFalse("Поле 'success' не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение о необходимости авторизации не соответствует ожидаемому"
                , "You should be authorised", body.path("message"));
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
