package com.github.teamfusion.spyglassplus.core;

import com.github.teamfusion.spyglassplus.common.entity.SpyglassStandEntity;

public interface ISpyable {

	void setCommand(boolean command);

	boolean isCommand();

	void setCommandTick(int tick);

	int getCommandTick();

	void setSpyglassStands(SpyglassStandEntity entity);

	SpyglassStandEntity getSpyGlassStands();

	void setCameraRotX(float cameraRotX);

	void setCameraRotY(float cameraRotY);

	float getCameraRotX();

	float getCameraRotY();
}
