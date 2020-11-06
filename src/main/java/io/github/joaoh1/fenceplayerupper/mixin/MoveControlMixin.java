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
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;",
            shift = At.Shift.BY,
            by = 2
        ),
        method = "tick()V",
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void ignoreFenceLimitations(CallbackInfo ci, double d, double e, double o, float q, BlockPos blockPos, BlockState blockState) {
        if (this.entity.getType().isIn(UpperUtils.ALLOWED_ENTITIES)) {
            //if (o > (double)this.entity.stepHeight && d * d + e * e < (double)Math.max(1.0F, this.entity.getWidth())) return;
            if (blockState.isIn(BlockTags.FENCES) && blockState.isIn(UpperUtils.BOOST_JUMP)) {
                this.entity.getJumpControl().setActive();
                this.state = MoveControl.State.JUMPING;
            }
        }
    }
}
