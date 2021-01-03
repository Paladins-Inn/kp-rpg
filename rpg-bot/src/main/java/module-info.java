module de.kaiserpfalzedv.rpg.bot {
    exports de.kaiserpfalzedv.rpg.bot;
    exports de.kaiserpfalzedv.rpg.bot.dice;

    opens de.kaiserpfalzedv.rpg.bot.dice to de.kaiserpfalzedv.rpg.bot;

    requires org.slf4j;
    requires de.kaiserpfalzedv.rpg.torg;
    requires JDA;
    requires microprofile.config.api;
    requires java.annotation;
    requires jakarta.inject.api;
    requires jakarta.enterprise.cdi.api;
    requires java.ws.rs;
    requires quarkus.core;
    requires de.kaiserpfalzedv.rpg.core;
}