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
package disconsented.cristallum.block;

import disconsented.cristallum.EnumSection;
import disconsented.cristallum.common.Logging;
import disconsented.cristallum.tileEntity.TileRefineryBase;
import disconsented.cristallum.tileEntity.TileRefineryComponent;
import disconsented.cristallum.tileEntity.TileRefineryCore;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class BlockRefinery extends BlockRefineryBase {
    //public static final BlockRefinery instance = new BlockRefinery("refinery");

    public static BlockRefinery instance;


    public BlockRefinery(String name){
        super(name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PROPERTY_DIRECTION, EnumFacing.NORTH).withProperty(PROPERTY_SECTION, EnumSection.PROGRESS));

    }



    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileRefineryBase tileRefineryBase = (TileRefineryBase)worldIn.getTileEntity(pos);
        if(tileRefineryBase != null){
            EnumSection enumSection = tileRefineryBase.section;
            EnumFacing enumFacing = tileRefineryBase.facing;
            if(enumSection != null && enumFacing != null){
                IBlockState newState = state.withProperty(PROPERTY_SECTION, enumSection).withProperty(PROPERTY_DIRECTION, enumFacing);
                return newState;
            }

        }
        return state;
    }

    @Override
    public boolean isOpaqueCube() { return false; }

    @Override
    public boolean isFullCube() { return false; }

    @Override
    public boolean isVisuallyOpaque() { return true; }

    /*
    * Assumes that the state it was created with is the progress block
    * */
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if((EnumSection)state.getValue(PROPERTY_SECTION) != EnumSection.PROGRESS){
            return;
        }
        EnumFacing facing = (EnumFacing)state.getValue(PROPERTY_DIRECTION);
        //Create the blocks in the area
        //Top to bottom
        //Cone side facing towards the direction

        //North -Z
        //South +z
        //Vert  Y
        //West -x
        //East +x


        switch (facing){
            case NORTH:
                //corner 1, down 2, + 1 to side, forward one
                setStateWithTileEntity(new BlockPos(pos.getX() + 1, pos.getY() - 2, pos.getZ() - 1),
                        getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.CORNER), worldIn, pos);
                //corner 2, down 2, - 1 to side, forward one
                setStateWithTileEntity(new BlockPos(pos.getX() - 1, pos.getY() - 2, pos.getZ() - 1),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.CORNER2), worldIn, pos);
                                //Progress, skipped
                                //Power, down 1
                setStateWithTileEntity(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.POWER), worldIn, pos);

                //crystal tank, down 1, + 1 to side
                setStateWithTileEntity(new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.TANK_CRYSTAL), worldIn, pos);

                //water tank, down 1, - 1 to side
                setStateWithTileEntity(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.TANK_WATER), worldIn, pos);

                //crystal input, down 2, + 1 to side
                setStateWithTileEntity(new BlockPos(pos.getX() + 1, pos.getY() - 2, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.INPUT_CRYSTAL), worldIn, pos);

                //Water input, down 2, -1 to side
                setStateWithTileEntity(new BlockPos(pos.getX() - 1, pos.getY() - 2, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.INPUT_WATER), worldIn, pos);

                //RF input, down 2
                setStateWithTileEntity(new BlockPos(pos.getX(), pos.getY() - 2, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.INPUT_RF), worldIn, pos);

                //Output
                setStateWithTileEntity(new BlockPos(pos.getX(), pos.getY() - 2, pos.getZ() - 1),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.OUTPUT_RESOURCE), worldIn, pos);

                break;
            case SOUTH:
                break;
            case EAST:
                break;
            case WEST:/*
                //corner 1, down 2, + 1 to side, forward one
                positions.add(new BlockPos(pos.getX() - 1, pos.getY() - 2, pos.getZ() + 1));
                //corner 2, down 2, - 1 to side, forward one
                positions.add(new BlockPos(pos.getX() -1, pos.getY() - 2, pos.getZ() - 1));
                //Progress, skipped
                //Power, down 1
                positions.add(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));
                //crystal tank, down 1, + 1 to side
                positions.add(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() + 1));
                //water tank, down 1, - 1 to side
                positions.add(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() - 1));
                //crystal input, down 2, + 1 to side
                positions.add(new BlockPos(pos.getX() - 1, pos.getY() - 2, pos.getZ() + 1));
                //Water input, down 2, -1 to side
                positions.add(new BlockPos(pos.getX(), pos.getY() - 2, pos.getZ() - 1));
                //RF input, down 2
                positions.add(new BlockPos(pos.getX(), pos.getY() - 2, pos.getZ()));
                //Output
                positions.add(new BlockPos(pos.getX() - 1, pos.getY() - 2, pos.getZ()));*/
                break;
        }
    }

    private void setStateWithTileEntity(BlockPos pos, IBlockState state, World worldIn, BlockPos corePos){
        Boolean setSuccess = worldIn.setBlockState(pos, state);
        TileEntity entity = worldIn.getTileEntity(pos);
        if(entity instanceof TileRefineryComponent){
            ((TileRefineryComponent) entity).setCore((TileRefineryCore)worldIn.getTileEntity(corePos));
        }
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {PROPERTY_DIRECTION, PROPERTY_SECTION});
    }

    /*
    Creating the appropriate TE's are
    We don't know where the rest of the blocks are
     */
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        EnumSection section = (EnumSection)state.getValue(PROPERTY_SECTION);
        if(!world.isRemote){
            System.out.println("Server:"+section);
        }else{
            System.out.println("Client:"+section);
        }
        EnumFacing facing = (EnumFacing)state.getValue(PROPERTY_DIRECTION);
        if(section == EnumSection.PROGRESS){
            return new TileRefineryCore(facing, section);
        } else {
            return new TileRefineryComponent(facing, section);
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }
}