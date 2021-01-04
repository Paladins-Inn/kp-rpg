/**
 * @author klenkes74
 * @since 1.0.0 2021-01-03
 */
module de.kaiserpfalzedv.rpg.traveller {
    requires de.kaiserpfalzedv.rpg.core;
    requires org.slf4j;
    requires jakarta.inject.api;
    requires jakarta.enterprise.cdi.api;

    exports de.kaiserpfalzedv.rpg.traveller.spacecrafts;
}