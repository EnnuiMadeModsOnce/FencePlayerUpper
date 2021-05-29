/*
 * Fence Player Upper
 * Copyright (C) 2020-2021 boredomh1
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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io.github.joaoh1.fenceplayerupper.utils.UpperUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.chunk.ChunkCache;

@Mixin(LandPathNodeMaker.class)
public abstract class LandPathNodeMakerMixin extends PathNodeMaker {
    private static MobEntity mobEntity;

    @Inject(at = @At("TAIL"), method = "init")
    public void getMobEntity(ChunkCache cachedWorld, MobEntity entity, CallbackInfo ci) {
        mobEntity = this.entity;
    }

    @Inject(at = @At("RETURN"), method = "getCommonNodeType", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static PathNodeType getCommonNodeTypeMixin(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<PathNodeType> cir, BlockState blockState) {
        PathNodeType returnedType = cir.getReturnValue();
        if (mobEntity != null && returnedType == PathNodeType.FENCE) {
            if (mobEntity.getType().isIn(UpperUtils.ALLOWED_ENTITIES)) {
                if (blockState.isIn(UpperUtils.BOOST_JUMP)) {
                    returnedType = PathNodeType.WALKABLE;
                    cir.setReturnValue(returnedType);
                }
            }
        }
        return returnedType;
    }
}
