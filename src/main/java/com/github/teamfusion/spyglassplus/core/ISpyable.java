package com.github.teamfusion.spyglassplus.core;

import com.github.teamfusion.spyglassplus.common.entity.SpyglassStandEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ISpyable {

	void setCommand(boolean command);

	boolean isCommand();

	void setCommandTick(int tick);

	int getCommandTick();

	void setSpyglassStands(SpyglassStandEntity entity);

	SpyglassStandEntity getSpyGlassStands();

	@OnlyIn(Dist.CLIENT)
	void setCameraRotX(float cameraRotX);

	@OnlyIn(Dist.CLIENT)
	void setCameraRotY(float cameraRotY);

	float getCameraRotX();

	float getCameraRotY();
}
