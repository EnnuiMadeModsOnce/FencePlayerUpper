package io.github.ennuil.fenceplayerupper.utils;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UpperUtils {
	public static final Tag<Block> BOOST_JUMP = TagFactory.BLOCK.create(new Identifier("fenceplayerupper", "boost_jump"));

	public static final boolean canJumpFence(World world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (world instanceof ServerWorld serverWorld) {
			if (blockState.isIn(BOOST_JUMP)) {
				serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5, 1, 0.0, 0.0, 0.0, 1);
			} else {
				serverWorld.spawnParticles(ParticleTypes.ANGRY_VILLAGER, pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5, 1, 0.0, 0.0, 0.0, 1);
			}
		}
		return blockState.isIn(BOOST_JUMP);
	}

	public static final BlockPos[] createFencePosArray(BlockPos pos, double x, double z) {
		return new BlockPos[] {
			pos,
			pos.add(MathHelper.floor(x), 0, MathHelper.floor(z)),
			pos.add(MathHelper.ceil(x), 0, MathHelper.ceil(z)),
			pos.add(MathHelper.floor(x * 2), 0, MathHelper.floor(z * 2)),
			pos.add(MathHelper.ceil(x * 2), 0, MathHelper.ceil(z * 2)),
			pos.add(MathHelper.floor(x * 3), 0, MathHelper.floor(z * 3)),
			pos.add(MathHelper.ceil(x * 3), 0, MathHelper.ceil(z * 3)),
			pos.add(MathHelper.floor(x * 4), 0, MathHelper.floor(z * 4)),
			pos.add(MathHelper.ceil(x * 4), 0, MathHelper.ceil(z * 4)),
			pos.add(x * 5, 0, z * 5)
		};
	}
}
