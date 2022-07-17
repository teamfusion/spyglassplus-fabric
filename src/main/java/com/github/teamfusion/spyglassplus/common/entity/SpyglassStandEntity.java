package com.github.teamfusion.spyglassplus.common.entity;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusItems;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class SpyglassStandEntity extends Entity {
	private static final EntityDataAccessor<Boolean> DATA_IS_HIGH = SynchedEntityData.defineId(SpyglassStandEntity.class, EntityDataSerializers.BOOLEAN);

	private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(SpyglassStandEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(SpyglassStandEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<ItemStack> DATA_SPYGLASS = SynchedEntityData.defineId(SpyglassStandEntity.class, EntityDataSerializers.ITEM_STACK);

	private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_ID = SynchedEntityData.defineId(SpyglassStandEntity.class, EntityDataSerializers.OPTIONAL_UUID);


	public SpyglassStandEntity(EntityType<? extends SpyglassStandEntity> p_20966_, Level p_20967_) {
		super(p_20966_, p_20967_);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> p_20059_) {
		if (DATA_IS_HIGH.equals(p_20059_)) {
			this.refreshDimensions();
		}
		super.onSyncedDataUpdated(p_20059_);
	}

	@Override
	public void tick() {
		super.tick();

		if (this.getOwner() != null) {
			this.getSpyGlass().onUseTick(this.level, this.getOwner(), 0);
			this.getSpyGlass().inventoryTick(this.level, this.getOwner(), 0, false);
		}

		this.move(MoverType.SELF, this.getDeltaMovement());
		if ((this.onGround || this.horizontalCollision)) {
			this.setDeltaMovement(Vec3.ZERO);
		} else {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
		}

		double d1 = 0.92D;
		this.setDeltaMovement(this.getDeltaMovement().scale(0.92D));
		this.reapplyPosition();

		if (this.getHurtTime() > 0) {
			this.setHurtTime(this.getHurtTime() - 1);
		}

		if (this.getDamage() > 0.0F) {
			this.setDamage(this.getDamage() - 1.0F);
		}
	}

	public boolean hurt(DamageSource damagesource, float damage) {
		if (!this.level.isClientSide && !this.isRemoved()) {
			if (this.isInvulnerableTo(damagesource)) {
				return false;
			} else {
				this.setHurtTime(10);
				this.markHurt();
				this.setDamage(this.getDamage() + damage * 10.0F);
				this.gameEvent(GameEvent.ENTITY_DAMAGED, damagesource.getEntity());
				boolean flag = damagesource.getEntity() instanceof Player && ((Player) damagesource.getEntity()).getAbilities().instabuild;
				if (flag || this.getDamage() > 40.0F) {
					this.ejectPassengers();
					if (!this.getSpyGlass().isEmpty()) {
						this.spawnAtLocation(this.getSpyGlass());
					}
					this.spawnAtLocation(this.getPickResult());
					this.discard();
				}

				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {

		if (this.getSpyGlass().isEmpty()) {
			if (player.getItemInHand(hand).is(Items.SPYGLASS)) {
				this.setSpyGlass(player.getItemInHand(hand).copy());
				player.getItemInHand(hand).shrink(1);
				this.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
				return InteractionResult.SUCCESS;
			} else if (player.isShiftKeyDown()) {
				this.setHigh(!this.isHigh());
				this.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
				return InteractionResult.SUCCESS;
			}
		} else {
			if (player.isShiftKeyDown()) {
				this.spawnAtLocation(this.getSpyGlass());
				this.setSpyGlass(ItemStack.EMPTY);
				this.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);

				return InteractionResult.SUCCESS;
			} else {
				if (this.level.isClientSide() && ((ISpyable) player).getSpyGlassStands() == null) {
					if (Minecraft.getInstance().player == player) {
						Minecraft.getInstance().setCameraEntity(this);
					}
				}
				this.setOwner(player);
				((ISpyable) player).setSpyglassStands(this);
				this.setYRot(player.getYRot());
				this.setXRot(0.0F);

				this.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
				return InteractionResult.CONSUME;

			}
		}

		return super.interact(player, hand);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_IS_HIGH, true);
		this.entityData.define(DATA_ID_HURT, 0);
		this.entityData.define(DATA_ID_DAMAGE, 0.0F);
		this.entityData.define(DATA_SPYGLASS, ItemStack.EMPTY);
		this.entityData.define(DATA_OWNER_ID, Optional.empty());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_20052_) {
		this.setHigh(p_20052_.getBoolean("IsHigh"));
		if (p_20052_.contains("Spyglass")) {
			ItemStack itemstack = ItemStack.of(p_20052_.getCompound("Spyglass"));
			this.setSpyGlass(itemstack);
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_20139_) {
		p_20139_.putBoolean("IsHigh", this.isHigh());
		ItemStack itemstack = this.getSpyGlass();
		if (!itemstack.isEmpty()) {
			p_20139_.put("Spyglass", itemstack.save(new CompoundTag()));
		}
	}

	@Override
	public float getViewXRot(float p_20268_) {
		if (this.getOwner() != null && this.getOwner() instanceof ISpyable) {
			return this.getXRot() + ((ISpyable) this.getOwner()).getCameraRotX() % 360;
		}
		return super.getViewXRot(p_20268_);
	}

	@Override
	public float getViewYRot(float p_20268_) {
		if (this.getOwner() != null && this.getOwner() instanceof ISpyable) {
			return this.getYRot() + ((ISpyable) this.getOwner()).getCameraRotY() % 360;
		}
		return super.getViewYRot(p_20268_);
	}

	public Vec3 getLookAngle() {
		if (this.getOwner() != null && this.getOwner() instanceof ISpyable) {
			return this.calculateViewVector(this.getXRot() + ((ISpyable) this.getOwner()).getCameraRotX() % 360, this.getYRot() + ((ISpyable) this.getOwner()).getCameraRotY() % 360);
		}
		return this.calculateViewVector(this.getXRot(), this.getYRot());
	}

	public void setHigh(boolean high) {
		this.entityData.set(DATA_IS_HIGH, high);
	}

	public boolean isHigh() {
		return this.entityData.get(DATA_IS_HIGH);
	}

	public void setDamage(float p_38110_) {
		this.entityData.set(DATA_ID_DAMAGE, p_38110_);
	}

	public float getDamage() {
		return this.entityData.get(DATA_ID_DAMAGE);
	}

	public void setHurtTime(int p_38155_) {
		this.entityData.set(DATA_ID_HURT, p_38155_);
	}

	public int getHurtTime() {
		return this.entityData.get(DATA_ID_HURT);
	}

	public void setSpyGlass(ItemStack spyGlass) {
		this.entityData.set(DATA_SPYGLASS, spyGlass);
	}

	public ItemStack getSpyGlass() {
		return this.entityData.get(DATA_SPYGLASS);
	}

	public void setOwner(Entity entity) {
		if (entity == null) {
			this.entityData.set(DATA_OWNER_ID, Optional.empty());
		} else {
			this.entityData.set(DATA_OWNER_ID, Optional.of(entity.getUUID()));
		}
	}

	@Nullable
	public Player getOwner() {
		Optional<UUID> optional = this.entityData.get(DATA_OWNER_ID);
		if (optional.isPresent()) {
			Player entity = this.level.getPlayerByUUID(optional.get());
			return entity;
		}
		return null;
	}

	public EntityDimensions getDimensions(Pose p_19721_) {
		return !this.isHigh() ? EntityDimensions.fixed(0.3F, 0.6F) : this.getType().getDimensions();
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Nullable
	@Override
	public ItemStack getPickResult() {
		return new ItemStack(SpyglassPlusItems.SPYGLASS_STAND.get());
	}

	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}


}
