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

import java.io.*;

public class StructBlockLocation {
    public Block block;
    public String blockName;
    public int x;
    public int y;
    public int z;

    public StructBlockLocation(Block block, String blockName, int x, int y, int z) {
        this.block = block;
        this.blockName = blockName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private StructBlockLocation(){}

    private void grabBlock(){
        block = (Block)Block.blockRegistry.getObject(blockName);
    }

    public NBTTagCompound toNBBTagCompound(){
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("blockName", blockName);
        nbtTagCompound.setInteger("x",x);
        nbtTagCompound.setInteger("y",y);
        nbtTagCompound.setInteger("z",z);
        return nbtTagCompound;
    }

    public static StructBlockLocation fromNBBTagCompound(NBTTagCompound nbtTagCompound){
        StructBlockLocation structBlockLocation = new StructBlockLocation();
        structBlockLocation.blockName = nbtTagCompound.getString("blockName");
        structBlockLocation.x = nbtTagCompound.getInteger("x");
        structBlockLocation.y = nbtTagCompound.getInteger("y");
        structBlockLocation.z = nbtTagCompound.getInteger("z");

        structBlockLocation.grabBlock();

        return structBlockLocation;
    }
}