/*
 * Fence Player Upper
 * Copyright (C) 2020 joaoh1
 *
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.joaoh1.fenceplayerupper.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.joaoh1.fenceplayerupper.FencePlayerUpperMod;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("RETURN"), method = "getJumpVelocity()F", cancellable = true)
	private float increaseJumpVelocity(CallbackInfoReturnable<Float> info) {
		float jumpVelocity = info.getReturnValueF();
		if (this.getType().isIn(FencePlayerUpperMod.ALLOWED_ENTITIES)) {
			BlockPos currentPos = this.getBlockPos();
			if (
				FencePlayerUpperMod.canJumpFence(this.world, currentPos) ||
				FencePlayerUpperMod.canJumpFence(this.world, currentPos.north()) ||
				FencePlayerUpperMod.canJumpFence(this.world, currentPos.south()) ||
				FencePlayerUpperMod.canJumpFence(this.world, currentPos.west()) ||
				FencePlayerUpperMod.canJumpFence(this.world, currentPos.east()) ||
				FencePlayerUpperMod.canJumpFence(this.world, currentPos.north().west()) ||
				FencePlayerUpperMod.canJumpFence(this.world, currentPos.south().west()) ||
				FencePlayerUpperMod.canJumpFence(this.world, currentPos.north().east()) ||
				FencePlayerUpperMod.canJumpFence(this.world, currentPos.south().east())
			) {
				if (!this.world.isClient) {
					jumpVelocity -= 0.03F;
				}
				this.velocityModified = true;
				info.setReturnValue(jumpVelocity);
			}
		}
		return jumpVelocity;
	}
}
