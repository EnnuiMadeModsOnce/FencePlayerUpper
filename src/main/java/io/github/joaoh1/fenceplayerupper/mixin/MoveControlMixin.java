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

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io.github.joaoh1.fenceplayerupper.utils.UpperUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;

@Mixin(MoveControl.class)
public class MoveControlMixin {
    @Shadow
    @Final
    private MobEntity entity;

    @Shadow
    private MoveControl.State state;

    @Inject(
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"
        ),
        method = "tick()V",
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void ignoreFenceLimitations(CallbackInfo ci, double d, double e, double o, float q, BlockPos blockPos, BlockState blockState) {
        if (this.entity.getType().isIn(UpperUtils.ALLOWED_ENTITIES)) {
            //if (o > (double)this.entity.stepHeight && d * d + e * e < (double)Math.max(1.0F, this.entity.getWidth())) return;
            //System.out.println(blockState.getBlock().toString());
            if (blockState.isIn(BlockTags.FENCES) && blockState.isIn(UpperUtils.BOOST_JUMP)) {
                this.entity.getJumpControl().setActive();
                this.state = MoveControl.State.JUMPING;
            } else {
                BlockPos blockPos2 = this.entity.getBlockPos().offset(this.entity.getHorizontalFacing());
                BlockState blockState2 = this.entity.world.getBlockState(blockPos2);
                if (blockState2.isIn(BlockTags.FENCES) && blockState2.isIn(UpperUtils.BOOST_JUMP)) {
                    this.entity.getJumpControl().setActive();
                    this.state = MoveControl.State.JUMPING;
                }
            }
        }
    }
}
