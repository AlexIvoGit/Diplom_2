import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

@DisplayName("Изменение данных пользователя")
public class ChangedUserDataTest {
    private UserClient userClient = new UserClient();
    private Map<String, String> userData;
    private String accessToken = null;

    @Before
    public void setUp() {
        userData = userClient.getMapGeneratedDataUser();
        accessToken = userClient
                .createUser(userData)
                .then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Изменение имени с авторизацией")
    public void changedUserNameWithAuthorization() {
        userData.put("name", RandomStringUtils.randomAlphabetic(10));
        ResponseBodyExtractionOptions body = userClient.changedUserData(userData, accessToken)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body();

        Assert.assertTrue("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Поле 'name' не соответствует данным пользователя", userData.get("name"), body.path("user.name"));
        Assert.assertEquals("Поле 'email' не соответствует данным пользователя", userData.get("email").toLowerCase(), body.path("user.email"));
    }

    @Test
    @DisplayName("Изменение email с авторизацией")
    public void changedUserEmailWithAuthorization() {
        userData.put("email", RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        ResponseBodyExtractionOptions body = userClient.changedUserData(userData, accessToken)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body();

        Assert.assertTrue("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Поле 'name' не соответствует данным пользователя", userData.get("name"), body.path("user.name"));
        Assert.assertEquals("Поле 'email' не соответствует данным пользователя", userData.get("email").toLowerCase(), body.path("user.email"));
    }

    @Test
    @DisplayName("Изменение имени без авторизации")
    public void changedUserNameWithoutAuthorization() {
        userData.put("name", RandomStringUtils.randomAlphabetic(10));
        ResponseBodyExtractionOptions body = userClient.changedUserData(userData, "")
                .then()
                .log().all()
                .statusCode(401)
                .extract()
                .body();

        Assert.assertFalse("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение, что пользователь не авторизован не соответствует ожидаемому"
                , "You should be authorised", body.path("message"));
    }

    @Test
    @DisplayName("Изменение email без авторизации")
    public void changedUserEmailWithoutAuthorization() {
        userData.put("email", RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        ResponseBodyExtractionOptions body = userClient.changedUserData(userData, "")
                .then()
                .log().all()
                .statusCode(401)
                .extract()
                .body();

        Assert.assertFalse("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение, что пользователь не авторизован не соответствует ожидаемому"
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
