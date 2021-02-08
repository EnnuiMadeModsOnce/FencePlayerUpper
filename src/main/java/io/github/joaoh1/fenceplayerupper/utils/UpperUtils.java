/*
 * Fence Player Upper
 * Copyright (C) 2020-2021 joaoh1
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

package io.github.joaoh1.fenceplayerupper.utils;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UpperUtils {
	public static final Tag<Block> BOOST_JUMP = TagRegistry.block(new Identifier("fenceplayerupper", "boost_jump"));
	public static final Tag<EntityType<?>> ALLOWED_ENTITIES = TagRegistry.entityType(new Identifier("fenceplayerupper", "allowed_entities"));

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
