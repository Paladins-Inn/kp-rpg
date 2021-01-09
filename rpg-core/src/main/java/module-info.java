/**
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2021-01-04
 */
module de.kaiserpfalzedv.rpg.core {
    exports de.kaiserpfalzedv.rpg.core.api;
    exports de.kaiserpfalzedv.rpg.core.cards;
    exports de.kaiserpfalzedv.rpg.core.dice;
    exports de.kaiserpfalzedv.rpg.core.files;
    exports de.kaiserpfalzedv.rpg.core.resources;

    opens de.kaiserpfalzedv.rpg.core.cards to com.fasterxml.jackson.databind;
    opens de.kaiserpfalzedv.rpg.core.files to com.fasterxml.jackson.databind;
    opens de.kaiserpfalzedv.rpg.core.resources to com.fasterxml.jackson.databind;

    requires org.slf4j;
    requires java.annotation;
    requires jakarta.enterprise.cdi.api;
    requires java.ws.rs;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires microprofile.openapi.api;
    requires exp4j;
    requires value;

    requires quarkus.mongodb.client;
    requires org.mongodb.driver.sync.client;
    requires quarkus.mongodb.panache;
    requires quarkus.mongodb.panache.common;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
}