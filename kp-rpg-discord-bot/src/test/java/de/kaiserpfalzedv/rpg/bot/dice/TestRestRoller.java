/*
 * Copyright (c) 2021 Kaiserpfalz EDV-Service, Roland T. Lichti.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.bot.dice;

import de.kaiserpfalzedv.commons.test.mongodb.MongoDBResource;
import de.kaiserpfalzedv.commons.test.oauth2.Oauth2WireMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
@QuarkusTestResource(Oauth2WireMock.class)
public class TestRestRoller {
    private static final String BEARER_TOKEN = "337aab0f-b547-489b-9dbd-a54dc7bdf20d";

    @Test
    public void shouldReturnARollWhenD6IsRolled() {
        given()
            .when()
                .header("Authorization", "bearer " + BEARER_TOKEN)
                .log().all()
                .get("/apis/die/v1/roll/D6")
                .prettyPeek()
            .then()
                .statusCode(200)
                .body(containsString("D6"));

    }
}
