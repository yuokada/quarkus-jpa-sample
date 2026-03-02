package io.github.yuokada.npb.player;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.yuokada.npb.test.PostgreSqlTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(PostgreSqlTestResource.class)
class PlayerTransferHistoryResourceTest {

    @Test
    void shouldGetTransferHistoryAndAppendEventByTransferApi() {
        List<Map<String, Object>> initialEvents = given()
            .when().get("/v1/player/1/transfers")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("$");

        assertFalse(initialEvents.isEmpty());
        Map<String, Object> firstEvent = initialEvents.get(0);
        assertNull(firstEvent.get("fromTeamId"));
        assertEquals(1, ((Number) firstEvent.get("toTeamId")).intValue());
        int initialCount = initialEvents.size();

        given()
            .contentType(ContentType.JSON)
            .body(Map.of("toTeamId", 2, "uniformNumber", 99))
            .when().post("/v1/player/1/transfers")
            .then()
            .statusCode(200)
            .body("team.id", is(2));

        List<Map<String, Object>> updatedEvents = given()
            .when().get("/v1/player/1/transfers")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("$");

        assertEquals(initialCount + 1, updatedEvents.size());
        Map<String, Object> lastEvent = updatedEvents.get(updatedEvents.size() - 1);
        assertEquals(1, ((Number) lastEvent.get("fromTeamId")).intValue());
        assertEquals(2, ((Number) lastEvent.get("toTeamId")).intValue());
    }

    @Test
    void shouldFilterTransferHistoryByFromToAndLimit() {
        List<Map<String, Object>> limited = given()
            .queryParam("from", "2024-01-01T00:00:00")
            .queryParam("to", "2024-01-01T00:00:00")
            .queryParam("limit", 1)
            .when().get("/v1/player/1/transfers")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("$");

        assertEquals(1, limited.size());
        assertNull(limited.get(0).get("fromTeamId"));
        assertEquals("2024-01-01T00:00:00", limited.get(0).get("transferredAt"));

        List<Map<String, Object>> empty = given()
            .queryParam("from", "2024-01-02T00:00:00")
            .when().get("/v1/player/1/transfers")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("$");
        assertTrue(empty.isEmpty());
    }
}
