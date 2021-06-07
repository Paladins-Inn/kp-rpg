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

package de.kaiserpfalzedv.rpg.torg.foundry.items;

import de.kaiserpfalzedv.rpg.torg.foundry.PriceMapper;
import de.kaiserpfalzedv.rpg.torg.model.core.Attack;
import de.kaiserpfalzedv.rpg.torg.model.core.Axiom;
import de.kaiserpfalzedv.rpg.torg.model.core.Damage;
import de.kaiserpfalzedv.rpg.torg.model.core.Skill;
import de.kaiserpfalzedv.rpg.torg.model.items.ItemData;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.Dependent;
import java.util.List;
import java.util.Set;

/**
 * AttackMapper --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-06-05
 */
@Dependent
@Slf4j
public class AttackMapper extends BaseItemMapper {

    public AttackMapper(@NotNull final PriceMapper priceMapper) {
        super(priceMapper);
    }

    @Override
    public ItemData convertItemSpec(
            @NotNull final ItemData.ItemDataBuilder result,
            @NotNull final FoundryItem orig
    ) {
        result.withAttack(convertAttacks(orig));

        return result.build();
    }

    private Set<Attack> convertAttacks(final FoundryItem orig) {
        return Set.of(convertAttack(orig));
    }

    private Attack convertAttack(final FoundryItem orig) {
        Attack.AttackBuilder result = Attack.builder();

        result.withName(orig.getName());
        result.withAttackNote(orig.getData().getNotes());
        result.withDamageNote(orig.getData().getNotes());
        convertTechlevel(orig, result);
        result.withSkill(Skill.mapFoundry(orig.getData().getAttackWith()).orElse(null));
        convertDamage(orig, result);
        result.withAp(orig.getData().getAp());
        convertAmmo(orig, result);
        convertRange(orig, result);

        return result.build();
    }

    private void convertTechlevel(FoundryItem orig, Attack.AttackBuilder result) {
        if (orig.getData().getTechlevel() != 0) {
            Axiom techAxiom = Axiom.builder()
                    .withName(Axiom.AxiomName.Tech)
                    .withValue(orig.getData().getTechlevel())
                    .build();
            result.withAxioms(List.of(techAxiom));

            log.trace("Set techlevel for this item. item='{}', id={}, level={}, axiom={}",
                    orig.getName(), orig.get_id(), orig.getData().getTechlevel(), techAxiom);
        } else {
            log.debug("No techlevel is given for item. item='{}', id={}",
                    orig.getName(), orig.get_id());
        }
    }

    private void convertDamage(FoundryItem orig, Attack.AttackBuilder result) {
        try {
            result.withDamage(
                    Damage.builder()
                            .withType(Damage.DamageType.mapFoundry(orig.getData().getDamageType()).orElse(null))
                            .withValue(orig.getData().getDamage())
                            .withAdds(orig.getData().getBonus())
                            .build()
            );
        } catch (NullPointerException e) {
            log.warn("No damage type defined for item. item='{}', id={}", orig.getName(), orig.get_id());
        }
    }

    private void convertAmmo(FoundryItem orig, Attack.AttackBuilder result) {
        try {
            result.withAmmunition(orig.getData().getAmmo().getMax());
        } catch (NullPointerException e) {
            log.debug("No ammunition given for item. item='{}', id={}", orig.getName(), orig.get_id());
        }
    }

    private void convertRange(FoundryItem orig, Attack.AttackBuilder result) {
        try {
            result.withRange(orig.getData().getRange());
        } catch (NullPointerException e) {
            log.debug("No range given for item. item='{}', id={}", orig.getName(), orig.get_id());
        }
    }
}
