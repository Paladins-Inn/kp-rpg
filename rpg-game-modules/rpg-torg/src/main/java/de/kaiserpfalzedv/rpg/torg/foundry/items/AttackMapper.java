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

import de.kaiserpfalzedv.rpg.torg.model.core.Attack;
import de.kaiserpfalzedv.rpg.torg.model.core.Axiom;
import de.kaiserpfalzedv.rpg.torg.model.core.Damage;
import de.kaiserpfalzedv.rpg.torg.model.core.Skill;
import de.kaiserpfalzedv.rpg.torg.model.items.ItemData;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

/**
 * AttackMapper --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-06-05
 */
@Slf4j
public class AttackMapper extends BaseItemMapper {
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

        if (orig.getData().getTechlevel() != 0) {
            result.withAxioms(List.of(
                    Axiom.builder()
                            .withName(Axiom.AxiomName.Tech)
                            .withValue(orig.getData().getTechlevel())
                            .build()
            ));
        }

        result.withSkill(Skill.mapFoundry(orig.getData().getAttackWith()).orElse(null));

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

        result.withAp(orig.getData().getAp());

        result.withDamageNote(orig.getData().getNotes());

        return result.build();
    }
}
