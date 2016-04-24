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

import disconsented.cristallum.EnumType;
import disconsented.cristallum.Reference;
import disconsented.cristallum.block.BlockCrystal;
import disconsented.cristallum.block.BlockSource;
import disconsented.cristallum.common.Logging;
import disconsented.cristallum.struct.BlockLocation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.ITickable;

import java.util.*;

public class TileSource extends TileEntity implements ITickable{
    public static final TileSource instance = new TileSource();
    public static final String name = "TileSource";
    private static final String TAGNAME = "STRUCTBLOCKLOCATION";
    private static final int radius = 4;
    private static final int verticalSearch = 6;

    private LinkedHashMap<Block,List<BlockLocation>> densityMap = new LinkedHashMap<>();
    private ArrayList<BlockLocation> densityList;

    private int ticks = 0;
    private int attempt = 0;
    private static final int attemptLimit = 5;
    private EnumType enumType;

    private boolean scanMode = false;

    public TileSource(){

    }

    public EnumType getEnumType(){
        if(enumType == null){
            enumType = (EnumType) getWorld().getBlockState(getPos()).getValue(BlockSource.PROPERTY_ENUM);
        }
            return enumType;
    }

    public void setScanMode(){
        scanMode = true;
        ticks = 0;
    }

    public void scan(){
        final net.minecraft.util.math.BlockPos pos = getPos();
        for (int x = pos.getX()-radius; x < radius+pos.getZ(); x++) {
            for (int y = 0; y < pos.getY(); y++) {
                for (int z = pos.getX()-radius; z < radius+pos.getZ(); z++) {
                    Block b = getWorld().getBlockState(new net.minecraft.util.math.BlockPos(x+pos.getX(),y,z+pos.getZ())).getBlock();
                    if(b instanceof BlockOre || b == Blocks.redstone_ore) {
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
        Logging.debug("Finished scanning source:"+pos.toString());
    }


    public void update() {
        if(scanMode){
            if(ticks >= 1200){
                scan();
                ticks = 0;
                scanMode = false;
            } else {
                ticks ++;
            }
            return;
        }
        if (ticks >= getEnumType().getTickRate()){
            ticks = 0;
            if(attempt < attemptLimit){
                if(densityMap != null && densityMap.size() > 0){
                    placeNext();
                }


            } else {
                attempt = 0;
            }
        } else {
            ticks++;
        }
        //spawnParticle();
    }

    private void spawnParticle() {
        getWorld().spawnParticle(EnumParticleTypes.PORTAL, getPos().getX()+ .5, getPos().getY()+1, getPos().getZ()+.5, 0, Reference.RANDOM.nextDouble(), 0, new int[0]);
    }

    private void placeNext(){
        attempt++;
        if(attempt > attemptLimit){
            return;
        }

        BlockLocation block = getBlockFromDensityMap(getEnumType().getWeight());

        /*for (int i = 0; i < 100; i++) {
            System.out.println("-.3,"+getBlockFromDensityMap(-.3).blockName);
        }
        for (int i = 0; i < 100; i++) {
            System.out.println("0,"+getBlockFromDensityMap(0).blockName);
        }
        for (int i = 0; i < 100; i++) {
            System.out.println(".3,"+getBlockFromDensityMap(.3).blockName);
        }*/


        if(block == null){
            placeNext();
            return;
        }


        World world = getWorld();
        int direction = Reference.RANDOM.nextInt(360);
        int depth  = Reference.RANDOM.nextInt(radius);
        int x = getPos().getX();
        int y = getPos().getY();
        int z = getPos().getZ();

        double newX = x + depth * Math.cos(direction);
        double newZ = z + depth * Math.sin(direction);

        //int rng = random.nextInt(3);
        //EnumType enumType = EnumType.byMetadata(rng);


        net.minecraft.util.math.BlockPos newPos = new net.minecraft.util.math.BlockPos(newX,y,newZ);
        if(pos.equals(newPos)){//So we don't replace the source, will not replace withing the same X,Z space so the source will always visible
            placeNext();
            return;
        }

        net.minecraft.util.math.BlockPos topPos = getTopBlock(newPos);
        //BlockPos topPos = getWorld().getTopSolidOrLiquidBlock(newPos);
        //Block topBlock = getWorld().getBlockState(topPos).getBlock();

        if(topPos.getY() >= y + verticalSearch && topPos.getY() <= y - verticalSearch ) {
            placeNext();
            return;
        }
        if(world.getBlockState(topPos).getBlock().equals(BlockCrystal.instance)){
            placeNext();
            return;
        }

        IBlockState blockState = world.getBlockState(getPos());
        if(blockState.getBlock().equals(BlockSource.instance)){
            EnumType type = (EnumType) blockState.getValue(BlockSource.PROPERTY_ENUM);
            IBlockState state = BlockCrystal.instance.getDefaultState();
            state = state.withProperty(BlockCrystal.PROPERTY_ENUM, type);
            Boolean success = world.setBlockState(topPos, state);//If we actually get through all of our checks
            TileCrystal crystal = (TileCrystal) world.getTileEntity(topPos);
            if(success && crystal != null && block != null) {
                crystal.block = block.block;

                Logging.debug("Creating a " + type.getName() + " at " + topPos.getX() + "," + topPos.getY() + "," + topPos.getZ() + " with " + block.blockName);
            } else {
                placeNext();
                return;
            }
        }

    }

    private BlockPos getTopBlock(net.minecraft.util.math.BlockPos pos){//TODO: Change this to find the first safe block to work from

        Chunk chunk = getWorld().getChunkFromBlockCoords(pos);
        net.minecraft.util.math.BlockPos blockpos;
        net.minecraft.util.math.BlockPos blockpos1;

        for (blockpos = new net.minecraft.util.math.BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos.getY() >= 0; blockpos = blockpos1)
        {
            blockpos1 = blockpos.down();
            IBlockState block = chunk.getBlockState(blockpos1);


            if (block.getMaterial().blocksMovement() && !block.getBlock().isLeaves(block, getWorld(), blockpos1) && !block.getBlock().isFoliage(getWorld(), blockpos1) && !(block instanceof BlockLiquid) && block != BlockCrystal.instance && block != BlockSource.instance)
            {
                break;
            }
        }

        return blockpos;
    }

    private BlockLocation getBlockFromDensityMap(double mod){
        if(densityList == null || densityList.size() == 0){
            densityList = new ArrayList<>();
            Iterator<List<BlockLocation>> iterator = densityMap.values().iterator();
            ArrayList<List<BlockLocation>> tempList = new ArrayList<>();
            while (iterator.hasNext()){
                List<BlockLocation> item = iterator.next();
                tempList.add(item);
            }
            tempList.sort(new Comparator<List<BlockLocation>>() {
                @Override
                public int compare(List<BlockLocation> o1, List<BlockLocation> o2) {
                    if(o1.size() > o2.size()){
                        return -1;
                    }
                    return 0;
                }
            });
            for (List<BlockLocation> locations : tempList){
                densityList.addAll(locations);
            }
        }

        int weightedInt = (int)getBiasedRandom(densityList.size()*mod,0, densityList.size());

        return densityList.get(weightedInt);
    }

    private double getBiasedRandom(double bias, double min, double max){
        double bias_depth_perc = 0.2;
        double bias_depth_abs = (max - min)*bias_depth_perc;
        double min_bias = bias - bias_depth_abs;
        double max_bias = bias + bias_depth_abs;

        if (max_bias > max) max_bias = max;
        if (min_bias < min) min_bias = min;

        double aVariance = (max_bias - min_bias)/2;


        double rndBiased = bias + Reference.RANDOM.nextGaussian() * aVariance;

        if (rndBiased > max)
            rndBiased = max - (rndBiased - max);

        if (rndBiased < min)
            rndBiased = min + (min - rndBiased);

        return rndBiased;
    }

    private int getWeightedInt(int max, double mod){//TODO: Refactor into http://stackoverflow.com/a/9947881
        max--;//Reduce the max value to be safe for getting out of the list.
        //int third = max / 3;
        if(1 > max)
            return 0;
        int random = Reference.RANDOM.nextInt(max);
        int weighted = (int) (random * mod);
        if(weighted > max){
            weighted = max;
        } else if(weighted < 0){
            weighted = 0;
        }
        return weighted;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList tagList = (NBTTagList)compound.getTag(TAGNAME);
        if(tagList == null) {
            return;
        }

        for (int i = 0; i < tagList.tagCount(); i++) {
            try{
                NBTTagCompound nbtTagCompound = (NBTTagCompound)tagList.get(i);
                //NBTTagCompound byteArray = (NBTTagByteArray)tagList.get(i);

                BlockLocation blockLocation =  BlockLocation.fromNBBTagCompound(nbtTagCompound);

                List<BlockLocation> blockLocationList = densityMap.get(blockLocation.block);
                if(blockLocationList == null){
                    ArrayList<BlockLocation> blockLocationArrayList = new ArrayList<BlockLocation>(){{add(blockLocation);}};
                    densityMap.put(blockLocation.block, blockLocationArrayList);
                } else {
                    blockLocationList.add(blockLocation);
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        Logging.debug("Finished reading from NBT at "+ getPos().toString());
        densityMap.forEach((block, blockLocations) -> {Logging.debug(block.getUnlocalizedName() +" #"+ blockLocations.size()); });

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
        Logging.debug("Finished writing to NBT at "+ getPos().toString());
        densityMap.forEach((block, blockLocations) -> {Logging.debug(block.getUnlocalizedName() +" #"+ blockLocations.size()); });
    }
}