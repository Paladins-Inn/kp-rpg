module de.kaiserpfalzedv.rpg.bot {
    exports de.kaiserpfalzedv.rpg.bot;
    exports de.kaiserpfalzedv.rpg.bot.dice;

    opens de.kaiserpfalzedv.rpg.bot.dice to de.kaiserpfalzedv.rpg.bot;

    requires de.kaiserpfalzedv.rpg.core;
    requires de.kaiserpfalzedv.rpg.torg;

    requires JDA;

    requires org.slf4j;
    requires quarkus.core;
    requires microprofile.config.api;
    requires java.ws.rs;
    requires java.annotation;
    requires jakarta.inject.api;
    requires jakarta.enterprise.cdi.api;
    requires jakarta.interceptor.api;
    requires quarkus.vertx;
    requires smallrye.common.annotation;
    requires smallrye.mutiny.vertx.core;
    requires io.smallrye.mutiny;
}