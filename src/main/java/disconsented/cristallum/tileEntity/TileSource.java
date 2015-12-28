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

import disconsented.cristallum.block.BlockRiparius;
import disconsented.cristallum.block.BlockSource;
import disconsented.cristallum.struct.BlockLocation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraft.util.ITickable;

import java.util.*;

public class TileSource extends TileEntity implements ITickable{
    public List<String> visible = new ArrayList<String>();

    public static final TileSource instance = new TileSource();
    public static final String name = "TileSource";
    private static final String TAGNAME = "STRUCTBLOCKLOCATION";
    private static final int radius = 16;
    private static final int verticalSearch = 6;
    private static final Random random = new Random();

    private LinkedHashMap<Block,List<BlockLocation>> densityMap = new LinkedHashMap<>();
    private List<BlockLocation> densityList = new ArrayList<>();

    private int ticks = 0;
    private int attempt = 0;
    private static final int attemptLimit = 5;



    public TileSource(){
        visible.add(OBJModel.Group.ALL);
    }

    public void scan(){
        BlockPos pos = getPos();
        for (int x = -8; x < 8; x++) {
            for (int y = 0; y < pos.getY(); y++) {
                for (int z = -8; z < 8; z++) {
                    Block b = getWorld().getBlockState(new BlockPos(x+pos.getX(),y,z+pos.getZ())).getBlock();
                    if(b instanceof BlockOre) {
                        List<BlockLocation> count = densityMap.get(b);

                        final BlockLocation blockLocation = new BlockLocation(b,x+pos.getX(),y,z+pos.getZ());

                        if (count == null) {
                            densityMap.put(b, new ArrayList(){{add(blockLocation);}});
                        } else {
                            count.add(blockLocation);
                        }
                    }
                }
            }
        }
    }


    public void update() {
        if (ticks >= 2){
            ticks = 0;
            if(attempt < attemptLimit){
                placeNext();
            } else {
                return;
            }
        } else {
            ticks++;
        }
    }

    private void placeNext(){
        int direction = random.nextInt(360);
        int depth  = random.nextInt(radius);
        int x = getPos().getX();
        int y = getPos().getY();
        int z = getPos().getZ();

        double newX = x + depth * Math.cos(direction);
        double newZ = z + depth * Math.sin(direction);
        IBlockState state = BlockRiparius.getStateById(BlockSource.getIdFromBlock(BlockRiparius.instance));

        BlockPos newPos = new BlockPos(newX,y,newZ);
        if(newPos.equals(getPos())){//So we don't replace the vein
            return;
        }
        BlockPos topPos = getWorld().getTopSolidOrLiquidBlock(newPos);
        if(topPos.getY() <= y) {
            getWorld().setBlockState(topPos, state);
        } else {
            placeNext();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList tagList = (NBTTagList)compound.getTag(TAGNAME);
        if(tagList == null) {
            return;
        }
        ArrayList<BlockLocation> list = new ArrayList<>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            try{
                NBTTagCompound nbtTagCompound = (NBTTagCompound)tagList.get(i);
                //NBTTagCompound byteArray = (NBTTagByteArray)tagList.get(i);
                list.add(BlockLocation.fromNBBTagCompound(nbtTagCompound));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if(list.size() > 0){
            densityList = list;
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if(densityMap.size() > 0){
            for (List<BlockLocation> list : densityMap.values()){
                NBTTagList nbtTagList = new NBTTagList();
                for (BlockLocation blockLocation : list){
                    try {
                        nbtTagList.appendTag(blockLocation.toNBBTagCompound());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    compound.setTag(TAGNAME, nbtTagList);
                }
            }
        }
    }
}