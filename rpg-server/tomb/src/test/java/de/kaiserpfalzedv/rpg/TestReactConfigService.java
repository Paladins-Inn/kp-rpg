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

package de.kaiserpfalzedv.rpg;

import de.kaiserpfalzedv.rpg.test.mongodb.MongoDBResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/**
 * Tests the retrieval of the Oauth2 client_id from the backend.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-18
 */
@QuarkusTest
@QuarkusTestResource(MongoDBResource.class)
public class TestReactConfigService {
    @Test
    public void shouldReturnARollWhenD6IsRolled() {
        given()
            .when()
                .get("/apis/config/v1/oauth2clientid")
            .prettyPeek()
            .then()
                .statusCode(200)
                .body(is("<client_id for user oauth2>"));

    }
}
