package com.github.teamfusion.spyglassplus.client.model;

import com.github.teamfusion.spyglassplus.common.entity.SpyglassStandEntity;
import net.minecraft.client.model.EntityModel;

public abstract class SpyglassStandBaseModel<T extends SpyglassStandEntity> extends EntityModel<T> {
	public boolean onlySpyglass;
}
