module de.kaiserpfalzedv.rpg.bot {
    exports de.kaiserpfalzedv.rpg.bot;

    requires org.slf4j;
    requires de.kaiserpfalzedv.rpg.torg;
    requires JDA;
    requires microprofile.config.api;
    requires java.annotation;
    requires jakarta.inject.api;
    requires java.ws.rs;
}