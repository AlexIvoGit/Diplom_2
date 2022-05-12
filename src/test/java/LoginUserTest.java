import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@DisplayName("Авторизация")
public class LoginUserTest {
    private UserClient userClient = new UserClient();
    private Map<String, String> dataUser = userClient.getMapGeneratedDataUser();
    private Map<String, String> loginData = new HashMap<>();
    private String accessToken = null;

    @Before
    public void setUp() {
        accessToken = userClient
                .createUser(dataUser)
                .then().extract().body().path("accessToken");

        loginData.put("email", dataUser.get("email"));
        loginData.put("password", dataUser.get("password"));
    }

    @Test
    @DisplayName("Логин пользователя с валидными данными")
    public void loginUserWithValidData() {
        ResponseBodyExtractionOptions body = userClient.loginUser(loginData)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body();

        Assert.assertTrue("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Поле 'email' при авторизации не соответствует ожидаемому", dataUser.get("email").toLowerCase(), body.path("user.email"));
        Assert.assertEquals("Поле 'name' при авторизации не соответствует ожидаемому", dataUser.get("name"), body.path("user.name"));
        Assert.assertNotNull("Поле 'accessToken' равняется NULL при при авторизации", body.path("accessToken"));
        Assert.assertNotNull("Поле 'refreshToken' равняется NULL при при авторизации", body.path("refreshToken"));
    }

    @Test
    @DisplayName("Логин пользователя с не верным паролем")
    public void loginUserWithNotValidPassword() {
        ResponseBodyExtractionOptions body = userClient.loginUser(loginData.get("email"), RandomStringUtils.randomAlphabetic(10))
                .then()
                .log().all()
                .statusCode(401)
                .extract()
                .body();

        Assert.assertFalse("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение, что 'email' или пароль не корретны не соответствует ожидаемому"
                , "email or password are incorrect", body.path("message"));
    }

    @Test
    @DisplayName("Логин пользователя с не верным email")
    public void loginUserWithNotValidEmail() {
        ResponseBodyExtractionOptions body = userClient.loginUser(RandomStringUtils.randomAlphabetic(10), loginData.get("password"))
                .then()
                .log().all()
                .statusCode(401)
                .extract()
                .body();

        Assert.assertFalse("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение, что 'email' или пароль не корретны не соответствует ожидаемому"
                , "email or password are incorrect", body.path("message"));
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
