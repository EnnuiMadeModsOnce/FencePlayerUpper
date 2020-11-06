package io.github.joaoh1.fenceplayerupper.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.joaoh1.fenceplayerupper.utils.UpperUtils;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin(AbstractBlockState.class)
public class MobNavigationMixin {
    @Inject(
        at = @At("RETURN"),
        method = "canPathfindThrough",
        cancellable = false
    )
    public boolean pathfindThroughFences(BlockView world, BlockPos pos, NavigationType type, CallbackInfoReturnable<Boolean> cir) {
        boolean returnedValue = cir.getReturnValueZ();
        if (returnedValue == false) {
            Block block = world.getBlockState(pos).getBlock();
            if (UpperUtils.BOOST_JUMP.contains(block)) {
                returnedValue = true;
                cir.setReturnValue(returnedValue);
            }
        }
        return returnedValue;
    }
}
