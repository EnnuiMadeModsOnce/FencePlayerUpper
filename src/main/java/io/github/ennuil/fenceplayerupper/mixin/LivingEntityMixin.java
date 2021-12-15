package io.github.ennuil.fenceplayerupper.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import io.github.ennuil.fenceplayerupper.utils.UpperUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	private LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyArg(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V"
		),
		method = "jump()V",
		index = 1
	)
	private double modifyJump(double originalJumpVelocity) {
		if (this.isPlayer()) {
			if (originalJumpVelocity <= 0.48) {
				BlockPos currentPos = this.getBlockPos();
				BlockPos[] positionsToCheck = UpperUtils.createFencePosArray(
					currentPos,
					this.getVelocity().multiply(20.0).getX(),
					this.getVelocity().multiply(20.0).getZ()
				);

				boolean boostJump = false;
				for (BlockPos blockPos : positionsToCheck) {
					//this.world.addParticle(ParticleTypes.ANGRY_VILLAGER, blockPos.getX() + 0.5, blockPos.getY() - 0.5, blockPos.getZ() + 0.5, 0, 0, 0);
					if (UpperUtils.canJumpFence(this.world, blockPos)) {
						boostJump = true;
						break;
					} else if (!this.world.getBlockState(blockPos).getCollisionShape(this.world, blockPos).isEmpty()) {
						this.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX() + 0.5, blockPos.getY() - 0.5, blockPos.getZ() + 0.5, 0, 0, 0);
						break;
					}
				}

				if ((LivingEntity)(Object)this instanceof ServerPlayerEntity) {
					System.out.println("Server: " + boostJump);
					System.out.println(this.getVelocity());
					if (boostJump) {
						if (!this.world.isClient) {
							originalJumpVelocity -= 0.03;
						}
						this.velocityModified = true;
					}
				} else {
					System.out.println("Client: " + boostJump);
					System.out.println(this.getVelocity());
				}
			}
		}

		return originalJumpVelocity;
	}
}
