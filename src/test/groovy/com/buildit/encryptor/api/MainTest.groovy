package com.buildit.encryptor.api

import com.jayway.restassured.RestAssured
import com.jayway.restassured.response.Response
import org.junit.BeforeClass
import org.junit.Test

import static ResourcePath.resourcePath
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat
import static com.jayway.restassured.RestAssured.given


class MainTest {


    public static final String URL = "http://localhost:4567/api"

    @BeforeClass
    static void setUp(){
        Main.main()
        RestAssured.baseURI = "localhost"
        RestAssured.port = 4567
    }

    @Test
    void shouldReturnEncryptedString() {
        Response response = given().
                formParam("password", "Sup3rS3cr3tStr1ng").
                formParam("secret", "833ac2fe-6343-11e7-aba5-dbb1a82b9a03").
                when().
                post(URL)

        assertThat(response.getStatusCode(), equalTo(200))
        assertThat(response.asString(), equalTo("DrtswuTw+cwvP3AZgd35MTwWPJ68LKy6"))
    }

    @Test
    void shouldReturnEncryptedStringFromFile() {
        Response response = given().
                multiPart(new File(resourcePath("secret_text_file.txt"))).
                formParam("secret", "833ac2fe-6343-11e7-aba5-dbb1a82b9a03").
                when().
                post(URL)

        assertThat(response.getStatusCode(), equalTo(200))
        assertThat(response.asString(), equalTo("stFankwE5eB/pMI2MmgnMzvL67owb7pMogUpq1tq888="))
    }

    @Test
    void shouldReturnErrorTextAndStatusCode() {
        Response response = given().
                multiPart(new File(resourcePath("secret_text_file.txt"))).
                formParam("secret", "").
                when().
                post(URL)

        assertThat(response.getStatusCode(), equalTo(400))
        assertThat(response.asString(), equalTo("A key to perform the encryption is required"))
    }
}
