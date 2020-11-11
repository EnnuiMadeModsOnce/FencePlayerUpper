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
        if (mobEntity == null) return returnedType;
        if (mobEntity.getType().isIn(UpperUtils.ALLOWED_ENTITIES)) {
            if (blockState.isIn(UpperUtils.BOOST_JUMP) && !blockState.getCollisionShape(blockView, blockPos).isEmpty()) {
                if (returnedType == PathNodeType.FENCE) {
                    returnedType = PathNodeType.WALKABLE;
                    cir.setReturnValue(returnedType);
                }
            }
        }
        return returnedType;
    } 
}
