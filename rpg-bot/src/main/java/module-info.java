module de.kaiserpfalzedv.rpg.bot {
    exports de.kaiserpfalzedv.rpg.bot;
    exports de.kaiserpfalzedv.rpg.bot.core;
    exports de.kaiserpfalzedv.rpg.bot.dice;

    requires de.kaiserpfalzedv.rpg.core;
    requires de.kaiserpfalzedv.rpg.torg;
    requires de.kaiserpfalzedv.rpg.hexxen;
    requires de.kaiserpfalzedv.rpg.fate;
    requires de.kaiserpfalzedv.rpg.saga;
    requires de.kaiserpfalzedv.rpg.traveller;
    requires de.kaiserpfalzedv.rpg.wod;
    requires de.kaiserpfalzedv.rpg.dsa5;

    // Discord Bot
    requires JDA;

    requires org.slf4j;

    requires quarkus.core;
    requires quarkus.vertx;
    requires quarkus.jackson;
    requires quarkus.resteasy;
    requires quarkus.resteasy.jackson;
    requires quarkus.resteasy.multipart;
    requires quarkus.scheduler;
    requires smallrye.common.annotation;
    requires smallrye.mutiny.vertx.core;
    requires io.smallrye.mutiny;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires microprofile.config.api;
    requires microprofile.health.api;
    requires microprofile.openapi.api;
    requires java.ws.rs;
    requires java.annotation;
    requires jakarta.inject.api;
    requires jakarta.enterprise.cdi.api;
    requires jakarta.interceptor.api;
}