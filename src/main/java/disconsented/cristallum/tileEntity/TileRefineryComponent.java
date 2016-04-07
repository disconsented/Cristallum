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
package disconsented.cristallum.tileEntity;

import disconsented.cristallum.EnumSection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * Common class for refinery pieces
 */
public class TileRefineryComponent extends TileRefineryBase{
    private BlockPos corePos;
    private TileRefineryCore core;
    public static final String name = "RefineryComponent";

    public TileRefineryComponent(EnumFacing facing, EnumSection section) {
        super(facing, section);
    }

    public TileRefineryComponent(){}


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        int x = compound.getInteger("X");
        int y = compound.getInteger("Y");
        int z = compound.getInteger("Z");
        corePos = new net.minecraft.util.math.BlockPos(x, y, z);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("X", corePos.getX());
        compound.setInteger("Y", corePos.getY());
        compound.setInteger("Z", corePos.getZ());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return new SPacketUpdateTileEntity(this.getPos(), 0, compound);
    }

    //Lack of information needed to do this on runtime
    /**
     * Sets the core so the component knows where to look.
     * @param core The core that will dictate other functions
     */
    public void setCore(TileRefineryCore core){
        this.core = core;
        corePos = core.getPos();
    }

    public TileRefineryCore getCore(){
        if(core == null){
            TileEntity worldCore = worldObj.getTileEntity(corePos);
            if(core instanceof TileRefineryCore){
                core = (TileRefineryCore)worldCore;
            }
        }
        return core;
    }
}