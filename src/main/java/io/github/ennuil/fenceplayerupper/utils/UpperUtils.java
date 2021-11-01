package io.github.ennuil.fenceplayerupper.utils;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UpperUtils {
	public static final Tag<Block> BOOST_JUMP = TagFactory.BLOCK.create(new Identifier("fenceplayerupper", "boost_jump"));

	public static final boolean canJumpFence(World world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		return blockState.isIn(BOOST_JUMP) && !blockState.getCollisionShape(world, pos).isEmpty();
	}

	public static final BlockPos[] createFencePosArray(BlockPos pos, double x, double z) {
		if (MathHelper.abs((float) x) == 0.75f && MathHelper.abs((float) z) == 0.75f) {
			x = (x / 3 * 4);
			z = (z / 3 * 4);
			return new BlockPos[] {
				pos,
				pos,
				pos.add(x, 0, z),
				pos.add(x * 2, 0, z * 2),
				pos.add(-x, 0, -z)
			};
		}

		return new BlockPos[] {
			pos,
			pos.add(x, 0, z),
			pos.add(x * 2, 0, z * 2),
			pos.add(x * 3, 0, z * 3),
			pos.add(-x, 0, -z)
		};
	}
}
