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
import disconsented.cristallum.block.BlockRefinery;
import disconsented.cristallum.block.BlockRefineryBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.util.ArrayList;

/**
 * Central T.E. records everything else
 */
public class TileRefineryCore extends TileRefineryBase implements ITickable{
    //Buffer
    private ItemStack[] inventory = new ItemStack[5];
    //Current crystal stack that is being processed
    private ItemStack processing;
    //How much RF is used per second to process a crystal
    private static final int rfPerTick = 0;
    //How many ticks it takes to process
    private static final int ticksToProcess = 20;
    private int rf = 0;
    private static final int rfMax = 20000;
    private int ticksElapsed = 0;
    public static final String name = "RefineryCore";

    /**Stores the other components
     * Input Crystal
     * Input Water
     * Input RF
     * Output Resource
     * Tank Crystal
     * Tank Water
     * RF indicator
     * Corner1
     * Corner2
     *
     * Process doesn't need to be stored as _this_ is the progress block.
     */
    private IBlockState[] pieces = new IBlockState[9];
    private boolean hasPieces = false;

    public TileRefineryCore(EnumFacing facing, EnumSection section) {
        super(facing, section);
    }

    @Override
    public void update() {
        /*
        BlockPos pos = getPos();
        if(!hasPieces){
            //Searching the area
            ArrayList<IBlockState> localPieces = new ArrayList<>();
            for (int x = pos.getX() - 1; x < pos.getX() + 1; x++) {
                for (int y = getPos().getY() - 2; y < pos.getY(); y++) {
                    for (int z = getPos().getZ() - 1 ; z < pos.getZ() + 1; z++) {
                        IBlockState currentPiece = getWorld().getBlockState(new BlockPos(x, y, z));
                        if(currentPiece.getBlock() == BlockRefinery.instance){
                            localPieces.add(currentPiece);
                        }
                    }
                }
            }
            //Sorting and adding to array
            for (IBlockState state : localPieces){
                EnumSection section = (EnumSection) state.getValue(BlockRefineryBase.PROPERTY_SECTION);
                switch (section){
                    case CORNER:
                        pieces[0] = state;
                        break;
                    case PROGRESS:
                        pieces[1] = state;
                        break;
                    case POWER:
                        pieces[2] = state;
                        break;
                    case TANK_CRYSTAL:
                        pieces[3] = state;
                        break;
                    case TANK_WATER:
                        pieces[4] = state;
                        break;
                    case INPUT_CRYSTAL:
                        pieces[5] = state;
                        break;
                    case INPUT_WATER:
                        pieces[6] = state;
                        break;
                    case INPUT_RF:
                        pieces[7] = state;
                        break;
                    case OUTPUT_RESOURCE:
                        pieces[8] = state;
                        break;
                }
            }
        }*/

        /*
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
        }*/
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