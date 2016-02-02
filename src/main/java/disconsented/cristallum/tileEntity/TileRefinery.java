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

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileRefinery extends TileEntity implements ITickable{
    private ItemStack[] inventory = new ItemStack[2];
    //How much RF is used per second to process a crystal
    private static final int rfPerTick = 0;
    //How many ticks it takes to process
    private static final int ticksToProcess = 20;
    private int rf = 0;
    private static final int rfMax = 20000;
    private int ticksElapsed = 0;
    @Override
    public void update() {
        if(rfPerTick < rf){//We have enough RF to do something
            if(inventory[1] != null){//Processing slot contains a crystal
                if(ticksElapsed >= ticksToProcess){
                    //Shits done place on ground or in container
                } else {
                    rf -= rfPerTick;
                }
            } else if(inventory[0] != null){//Waiting contains a crystal
                inventory[1] = inventory[0];//Moving it over
                inventory[0] = null;//Opening up the space
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

    }
}