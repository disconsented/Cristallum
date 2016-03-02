/*The MIT License (MIT)

Copyright (c) 2015 Disconsented, James Kerr

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
package disconsented.cristallum.worldgen;

import disconsented.cristallum.EnumType;
import disconsented.cristallum.block.BlockSource;
import disconsented.cristallum.tileEntity.TileSource;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGen implements IWorldGenerator{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        int worldID = world.provider.getDimensionId();
        if(worldID != -1 && worldID != 1) {
            int randX = random.nextInt(16) + chunkX*16;
            int randZ = random.nextInt(16) + chunkZ*16;
            for (int i = 62; i < 255; i++) {
                BlockPos pos = new BlockPos(randX, i, randZ);
                BlockPos pos1 = new BlockPos(randX, i-1, randZ);
                AxisAlignedBB bb = new AxisAlignedBB(randX-2,i-2,randZ-2,randX+2,i+2,randZ+2);
                Block underBlock = world.getBlockState(pos1).getBlock();
                int randInt = random.nextInt(100);
                if (world.isAirBlock(pos) &&
                        !world.isAnyLiquid(bb) &&
                        underBlock != BlockSource.instance &&
                        underBlock != Blocks.air &&
                        randInt == 2) {
                    IBlockState state = BlockSource.getStateById(BlockSource.getIdFromBlock(BlockSource.instance));
                    int rnd = random.nextInt(100);

                    if(rnd < 75){
                        state = state.withProperty(BlockSource.PROPERTY_ENUM, EnumType.RIPARIUS);
                    } else if(rnd < 95){
                        state = state.withProperty(BlockSource.PROPERTY_ENUM, EnumType.VINIFERA);
                    } else {
                        state = state.withProperty(BlockSource.PROPERTY_ENUM, EnumType.ABOREUS);
                    }
                    world.setBlockState(pos, state);

                    TileSource source = (TileSource)world.getTileEntity(pos);
                    source.scan();
                    source.getEnumType();


                    return;
                }
            }
        }

    }
}