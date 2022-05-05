package com.github.teamfusion.spyglassplus.core.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class SpyGlassVecUtils {
	@Nullable
	public static EntityHitResult getEntityHitWithNonClipResult(Entity p_37288_, Vec3 p_37289_, Vec3 p_37290_, AABB p_37291_, Predicate<Entity> p_37292_, double p_37293_) {
		Level level = p_37288_.level;
		double d0 = p_37293_;
		Entity entity = null;
		Vec3 vec3 = null;

		for (Entity entity1 : level.getEntities(p_37288_, p_37291_, p_37292_)) {
			AABB aabb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius());
			Optional<Vec3> optional = nonClipEntity(aabb, p_37289_, p_37290_);
			if (aabb.contains(p_37289_)) {
				if (d0 >= 0.0D) {
					entity = entity1;
					vec3 = optional.orElse(p_37289_);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vec3 vec31 = optional.get();
				double d1 = p_37289_.distanceToSqr(vec31);
				if (d1 < d0 || d0 == 0.0D) {
					if (entity1.getRootVehicle() == p_37288_.getRootVehicle() && !entity1.canRiderInteract()) {
						if (d0 == 0.0D) {
							entity = entity1;
							vec3 = vec31;
						}
					} else {
						entity = entity1;
						vec3 = vec31;
						d0 = d1;
					}
				}
			}
		}

		return entity == null ? null : new EntityHitResult(entity, vec3);
	}

	@Nullable
	public static Optional<Vec3> nonClipEntity(AABB aabb, Vec3 p_82372_, Vec3 p_82373_) {
		double[] adouble = new double[]{1.0D};
		double d0 = p_82373_.x - p_82372_.x;
		double d1 = p_82373_.y - p_82372_.y;
		double d2 = p_82373_.z - p_82372_.z;
		Direction direction = getNonClipDirection(aabb, p_82372_, adouble, (Direction) null, d0, d1, d2);
		if (direction == null) {
			return Optional.empty();
		} else {
			double d3 = adouble[0];
			return Optional.of(p_82372_.add(d3 * d0, d3 * d1, d3 * d2));
		}
	}

	@Nullable
	private static Direction getNonClipDirection(AABB p_82326_, Vec3 p_82327_, double[] p_82328_, @Nullable Direction p_82329_, double p_82330_, double p_82331_, double p_82332_) {
		if (p_82330_ > 1.0E-7D) {
			p_82329_ = nonClipPoint(p_82328_, p_82329_, p_82330_, p_82331_, p_82332_, p_82326_.minX, p_82326_.minY, p_82326_.maxY, p_82326_.minZ, p_82326_.maxZ, Direction.WEST, p_82327_.x, p_82327_.y, p_82327_.z);
		} else if (p_82330_ < -1.0E-7D) {
			p_82329_ = nonClipPoint(p_82328_, p_82329_, p_82330_, p_82331_, p_82332_, p_82326_.maxX, p_82326_.minY, p_82326_.maxY, p_82326_.minZ, p_82326_.maxZ, Direction.EAST, p_82327_.x, p_82327_.y, p_82327_.z);
		}

		if (p_82331_ > 1.0E-7D) {
			p_82329_ = nonClipPoint(p_82328_, p_82329_, p_82331_, p_82332_, p_82330_, p_82326_.minY, p_82326_.minZ, p_82326_.maxZ, p_82326_.minX, p_82326_.maxX, Direction.DOWN, p_82327_.y, p_82327_.z, p_82327_.x);
		} else if (p_82331_ < -1.0E-7D) {
			p_82329_ = nonClipPoint(p_82328_, p_82329_, p_82331_, p_82332_, p_82330_, p_82326_.maxY, p_82326_.minZ, p_82326_.maxZ, p_82326_.minX, p_82326_.maxX, Direction.UP, p_82327_.y, p_82327_.z, p_82327_.x);
		}

		if (p_82332_ > 1.0E-7D) {
			p_82329_ = nonClipPoint(p_82328_, p_82329_, p_82332_, p_82330_, p_82331_, p_82326_.minZ, p_82326_.minX, p_82326_.maxX, p_82326_.minY, p_82326_.maxY, Direction.NORTH, p_82327_.z, p_82327_.x, p_82327_.y);
		} else if (p_82332_ < -1.0E-7D) {
			p_82329_ = nonClipPoint(p_82328_, p_82329_, p_82332_, p_82330_, p_82331_, p_82326_.maxZ, p_82326_.minX, p_82326_.maxX, p_82326_.minY, p_82326_.maxY, Direction.SOUTH, p_82327_.z, p_82327_.x, p_82327_.y);
		}

		return p_82329_;
	}

	@Nullable
	private static Direction nonClipPoint(double[] p_82348_, @Nullable Direction p_82349_, double p_82350_, double p_82351_, double p_82352_, double p_82353_, double p_82354_, double p_82355_, double p_82356_, double p_82357_, Direction p_82358_, double p_82359_, double p_82360_, double p_82361_) {
		double d0 = (p_82353_ - p_82359_) / p_82350_;
		double d1 = p_82360_ + d0 * p_82351_;
		double d2 = p_82361_ + d0 * p_82352_;
		p_82348_[0] = d0;
		return p_82358_;
	}
}
