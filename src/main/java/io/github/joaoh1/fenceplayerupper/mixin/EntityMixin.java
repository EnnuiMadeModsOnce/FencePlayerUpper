package io.github.joaoh1.fenceplayerupper.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io.github.joaoh1.fenceplayerupper.utils.UpperUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    public World world;
    
    @Shadow
    public EntityType<?> getType() {
        return null;
    }

    @Inject(
        at = @At(
            value = "RETURN",
            ordinal = 1
        ),
        method = "getLandingPos()Lnet/minecraft/util/math/BlockPos;",
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    ) 
    private BlockPos ignoreFencesAndWalls(CallbackInfoReturnable<BlockPos> cir, int i, int j, int k, BlockPos blockPos) {
        BlockPos returnedBlockPos = cir.getReturnValue();
        if (this.getType().isIn(UpperUtils.ALLOWED_ENTITIES)) {
            BlockState blockState = this.world.getBlockState(blockPos.down());
            if (blockState.isIn(UpperUtils.BOOST_JUMP)) {
                returnedBlockPos = blockPos;
                cir.setReturnValue(returnedBlockPos);
            } 
        }
        return returnedBlockPos;
    }
}
