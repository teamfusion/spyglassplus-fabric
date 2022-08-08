package com.github.teamfusion.spyglassplus.api.entity;

import java.util.Optional;

public interface ScopingPlayer {
    default void setSpyglassStand(Integer id) {
    }

    default void setSpyglassStandEntity(SpyglassStandEntity entity) {
    }

    default Optional<Integer> getSpyglassStand() {
        return Optional.empty();
    }

    default Optional<SpyglassStandEntity> getSpyglassStandEntity() {
        return Optional.empty();
    }
}
