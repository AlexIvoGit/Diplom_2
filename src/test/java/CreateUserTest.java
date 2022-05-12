import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

@DisplayName("Создание пользователя")
public class CreateUserTest {
    private UserClient userClient = new UserClient();
    private Map<String, String> userData;
    private String accessToken = null;

    @Test
    @DisplayName("Создание пользователя")
    public void createUserTest() {
        userData = userClient.getMapGeneratedDataUser();

        ResponseBodyExtractionOptions body = userClient.createUser(userData)
                .then()
                .log().all()
                .statusCode(200)
                .extract().body();

        accessToken = body.path("accessToken");

        Assert.assertTrue("Поле 'success' при создании пользователя не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Поле 'email' не соответствует ожидаемому при создании пользователя", userData.get("email").toLowerCase(), body.path("user.email"));
        Assert.assertEquals("Поле 'name' не соответствует ожидаемому при создании пользователя", userData.get("name"), body.path("user.name"));
        Assert.assertNotNull("Поле 'accessToken' равняется NULL при создании пользователя", accessToken);
        Assert.assertNotNull("Поле 'refreshToken' равняется NULL при при авторизации", body.path("refreshToken"));
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    public void createDoubleUserTest() {
        userData = userClient.getMapGeneratedDataUser();

        Response user = userClient.createUser(userData);
        accessToken = user.then().extract().body().path("accessToken");

        ResponseBodyExtractionOptions body = userClient.createUser(userData)
                .then()
                .log().all()
                .statusCode(403)
                .extract().body();
        Assert.assertFalse("Поле 'success' при повторном создании пользователя не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение, что пользователь уже создан не соответствует ожидаемому"
                , "User already exists", body.path("message"));
    }

    @Test
    @DisplayName("Создание пользователя без указания 'EMAIL'")
    public void createUserWithoutEmail() {
        Map<String, String> mapGeneratedDataUser = userClient.getMapGeneratedDataUser();
        mapGeneratedDataUser.remove("email");
        ResponseBodyExtractionOptions body = userClient.createUser(mapGeneratedDataUser)
                .then()
                .log().all()
                .statusCode(403)
                .extract().body();
        Assert.assertFalse("Поле 'success' при создании пользователя без 'email' не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение при создании пользователя без 'email' не соответствует ожидаемому"
                , "Email, password and name are required fields", body.path("message"));
    }

    @Test
    @DisplayName("Создание пользователя без указания пароля")
    public void createUserWithoutPassword() {
        Map<String, String> mapGeneratedDataUser = userClient.getMapGeneratedDataUser();
        mapGeneratedDataUser.remove("password");
        ResponseBodyExtractionOptions body = userClient.createUser(mapGeneratedDataUser)
                .then()
                .log().all()
                .statusCode(403)
                .extract().body();
        Assert.assertFalse("Поле 'success' при создании пользователя без 'password' не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение при создании пользователя без 'password' не соответствует ожидаемому"
                , "Email, password and name are required fields", body.path("message"));

    }

    @Test
    @DisplayName("Создание пользователя без указания имени")
    public void createUserWithoutName() {
        Map<String, String> mapGeneratedDataUser = userClient.getMapGeneratedDataUser();
        mapGeneratedDataUser.remove("name");
        ResponseBodyExtractionOptions body = userClient.createUser(mapGeneratedDataUser)
                .then()
                .log().all()
                .statusCode(403)
                .extract().body();
        Assert.assertFalse("Поле 'success' при создании пользователя без 'name' не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение при создании пользователя без 'name' не соответствует ожидаемому"
                , "Email, password and name are required fields", body.path("message"));

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
