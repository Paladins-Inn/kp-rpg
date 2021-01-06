/**
 * @author klenkes74
 * @since 1.0.0 2021-01-03
 */
module de.kaiserpfalzedv.rpg.fate {
    exports de.kaiserpfalzedv.rpg.fate;
    exports de.kaiserpfalzedv.rpg.fate.dice;

    requires de.kaiserpfalzedv.rpg.core;
    requires org.slf4j;
    requires jakarta.inject.api;
    requires jakarta.enterprise.cdi.api;
}