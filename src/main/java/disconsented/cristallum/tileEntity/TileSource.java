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
import disconsented.cristallum.struct.StructBlockLocation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.model.obj.OBJModel;

import java.util.*;

public class TileSource extends TileEntity implements IUpdatePlayerListBox {
    public List<String> visible = new ArrayList<String>();

    public static final TileSource instance = new TileSource();
    public static final String name = "TileSource";
    private static final String TAGNAME = "STRUCTBLOCKLOCATION";
    private static final int radius = 8;
    private static final Random random = new Random();

    private LinkedHashMap<Block,List<StructBlockLocation>> densityMap = new LinkedHashMap<>();
    private List<StructBlockLocation> densityList = new ArrayList<>();

    private int ticks = 0;



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
                        List<StructBlockLocation> count = densityMap.get(b);
                        String name = Block.blockRegistry.getNameForObject(b).toString();

                        final StructBlockLocation structBlockLocation = new StructBlockLocation(b,name,x+pos.getX(),y,z+pos.getZ());

                        if (count == null) {
                            densityMap.put(b, new ArrayList(){{add(structBlockLocation);}});
                        } else {
                            count.add(structBlockLocation);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        if (ticks >= 2){
            ticks = 0;
            placeNext();
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

        IBlockState state = BlockRiparius.getStateById(BlockSource.getIdFromBlock(BlockRiparius.instance));

        BlockPos pos = new BlockPos((x + depth * Math.cos(direction)), y , (z + depth * Math.sin(direction)));

        if(pos.equals(getPos())){
            return;
        }
        getWorld().setBlockState(pos, state);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList tagList = (NBTTagList)compound.getTag(TAGNAME);
        if(tagList == null) {
            return;
        }
        ArrayList<StructBlockLocation> list = new ArrayList<>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            try{
                NBTTagCompound nbtTagCompound = (NBTTagCompound)tagList.get(i);
                //NBTTagCompound byteArray = (NBTTagByteArray)tagList.get(i);
                list.add(StructBlockLocation.fromNBBTagCompound(nbtTagCompound));
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
            for (List<StructBlockLocation> list : densityMap.values()){
                NBTTagList nbtTagList = new NBTTagList();
                for (StructBlockLocation structBlockLocation : list){
                    try {
                        nbtTagList.appendTag(structBlockLocation.toNBBTagCompound());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    compound.setTag(TAGNAME, nbtTagList);
                }
            }
        }
    }
}