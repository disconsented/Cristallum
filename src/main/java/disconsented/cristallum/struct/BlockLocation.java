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
package disconsented.cristallum.struct;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class BlockLocation {
    public Block block;
    public ResourceLocation blockName;
    public int x;
    public int y;
    public int z;

    public BlockLocation(Block block, int x, int y, int z) {
        this.block = block;
        blockName = Block.REGISTRY.getNameForObject(block);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private BlockLocation(){}

    private void grabBlock(){
        block = (Block)Block.REGISTRY.getObject(blockName);
    }

    public NBTTagCompound toNBBTagCompound(){
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("blockName", blockName.toString());
        nbtTagCompound.setInteger("x",x);
        nbtTagCompound.setInteger("y",y);
        nbtTagCompound.setInteger("z",z);
        return nbtTagCompound;
    }

    public static BlockLocation fromNBBTagCompound(NBTTagCompound nbtTagCompound){
        BlockLocation blockLocation = new BlockLocation();
        blockLocation.blockName = new ResourceLocation(nbtTagCompound.getString("blockName"));
        blockLocation.x = nbtTagCompound.getInteger("x");
        blockLocation.y = nbtTagCompound.getInteger("y");
        blockLocation.z = nbtTagCompound.getInteger("z");

        blockLocation.grabBlock();

        return blockLocation;
    }
}