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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.joaoh1.fenceplayerupper.utils.UpperUtils;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin(AbstractBlockState.class)
public class AbstractBlockStateMixin {
    @Inject(
        at = @At("RETURN"),
        method = "canPathfindThrough",
        cancellable = false
    )
    public boolean pathfindThroughFences(BlockView world, BlockPos pos, NavigationType type, CallbackInfoReturnable<Boolean> cir) {
        boolean returnedValue = cir.getReturnValueZ();
        if (returnedValue == false) {
            Block block = world.getBlockState(pos).getBlock();
            if (UpperUtils.BOOST_JUMP.contains(block) && BlockTags.FENCES.contains(block)) {
                returnedValue = true;
                cir.setReturnValue(returnedValue);
            }
        }
        return returnedValue;
    }
}
