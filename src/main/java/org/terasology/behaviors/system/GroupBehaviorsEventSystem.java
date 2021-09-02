// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.behaviors.system;

import org.terasology.behaviors.components.FleeingComponent;
import org.terasology.behaviors.components.GroupFleeOnHitComponent;
import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.behavior.GroupTagComponent;
import org.terasology.engine.logic.characters.CharacterMovementComponent;
import org.terasology.engine.registry.In;
import org.terasology.module.health.events.OnDamagedEvent;

/*
 * Listens for damage events and responds according to the group behavior desired
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class GroupBehaviorsEventSystem extends BaseComponentSystem {

    @In
    private Time time;
    @In
    private EntityManager entityManager;

    @ReceiveEvent(components = GroupFleeOnHitComponent.class)
    public void onDamage(OnDamagedEvent event, EntityRef entity) {

        //Get all entities belonging to the 'wiseDeers' group:
        for (EntityRef entityRef : entityManager.getEntitiesWith(GroupTagComponent.class)) {
            if (entityRef.getComponent(GroupTagComponent.class).groups.contains("wiseDeers")) {
                // Make entity flee
                FleeingComponent fleeingComponent = new FleeingComponent();
                fleeingComponent.instigator = event.getInstigator();
                fleeingComponent.minDistance = entityRef.getComponent(GroupFleeOnHitComponent.class).minDistance;
                entityRef.saveComponent(fleeingComponent);

                // Increase speed by multiplier factor
                CharacterMovementComponent characterMovementComponent = entityRef.getComponent(CharacterMovementComponent.class);
                characterMovementComponent.speedMultiplier = entityRef.getComponent(GroupFleeOnHitComponent.class).speedMultiplier;
                entityRef.saveComponent(characterMovementComponent);
            }
        }
    }
}
