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
import disconsented.cristallum.tileEntity.TileRefineryBase;
import disconsented.cristallum.tileEntity.TileRefineryComponent;
import disconsented.cristallum.tileEntity.TileRefineryCore;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRefinery extends BlockRefineryBase {
    //public static final BlockRefinery instance = new BlockRefinery("refinery");

    public static BlockRefinery instance;

    public static final PropertyInteger WATER_LEVEL = PropertyInteger.create("level", 0, 3);


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
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, net.minecraft.util.math.BlockPos pos) {
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            state = getActualState(state, worldIn, pos);
            EnumSection section = (EnumSection)state.getValue(PROPERTY_SECTION);
            if (section == EnumSection.INPUT_CRYSTAL) {
                TileRefineryComponent self = (TileRefineryComponent) worldIn.getTileEntity(pos);
                ItemStack inUse = playerIn.getHeldItem(EnumHand.MAIN_HAND);
                if (self.getCore().addItem(inUse)) {
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }

    @Override
    public boolean isVisuallyOpaque() { return true; }

    //Taken from https://github.com/MinecraftForge/MinecraftForge/blob/master/src/test/java/net/minecraftforge/debug/ModelLoaderRegistryDebug.java
    public static EnumFacing getFacingFromEntity(World worldIn, BlockPos clickedBlock, EntityLivingBase entityIn)
    {
        if (MathHelper.abs((float)entityIn.posX - (float)clickedBlock.getX()) < 2.0F && MathHelper.abs((float) entityIn.posZ - (float) clickedBlock.getZ()) < 2.0F)
        {
            double d0 = entityIn.posY + (double)entityIn.getEyeHeight();

            if (d0 - (double)clickedBlock.getY() > 2.0D)
            {
                return EnumFacing.NORTH;
            }

            if ((double)clickedBlock.getY() - d0 > 0.0D)
            {
                return EnumFacing.NORTH;
            }
        }

        return entityIn.getHorizontalFacing();
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, net.minecraft.util.math.BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(PROPERTY_DIRECTION, getFacingFromEntity(worldIn, pos, placer));
    }

    //Checking that the core still exists, if it doesnt we destory ourselves
    @Override
    public void onNeighborBlockChange(World worldIn, net.minecraft.util.math.BlockPos pos, IBlockState state, Block neighborBlock) {
        TileEntity entity = worldIn.getTileEntity(pos);
        if(entity instanceof TileRefineryComponent){
            TileRefineryCore core = ((TileRefineryComponent)entity).getCore();
            if(core == null){
                worldIn.setBlockState(pos, Blocks.air.getDefaultState());
            }
        }
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    //When one piece is destoryed we remove the core
    @Override
    public void onBlockDestroyedByPlayer(World worldIn, net.minecraft.util.math.BlockPos pos, IBlockState state) {
        TileEntity entity = worldIn.getTileEntity(pos);
        if(entity instanceof TileRefineryComponent){
            worldIn.setBlockState(((TileRefineryComponent)entity).getCore().getPos(), Blocks.air.getDefaultState());
        }
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    //Assumes that the state it was created with is the progress block
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
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX() + 1, pos.getY() - 2, pos.getZ() - 1),
                        getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.CORNER), worldIn, pos);
                //corner 2, down 2, - 1 to side, forward one
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX() - 1, pos.getY() - 2, pos.getZ() - 1),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.CORNER2), worldIn, pos);
                                //Progress, skipped
                                //Power, down 1
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.POWER), worldIn, pos);

                //crystal tank, down 1, + 1 to side
                setStateWithTileEntity(new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.TANK_CRYSTAL), worldIn, pos);

                //water tank, down 1, - 1 to side
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.TANK_WATER), worldIn, pos);

                //crystal input, down 2, + 1 to side
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX() + 1, pos.getY() - 2, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.INPUT_CRYSTAL), worldIn, pos);

                //Water input, down 2, -1 to side
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX() - 1, pos.getY() - 2, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.INPUT_WATER), worldIn, pos);

                //RF input, down 2
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX(), pos.getY() - 2, pos.getZ()),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.INPUT_RF), worldIn, pos);

                //Output
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX(), pos.getY() - 2, pos.getZ() - 1),
                getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.OUTPUT_RESOURCE), worldIn, pos);
                break;
            case SOUTH:
                //corner 1, down 2, + 1 to side, forward one
                setStateWithTileEntity(new BlockPos(pos.getX() - 1, pos.getY() - 2, pos.getZ() + 1),
                        getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.CORNER), worldIn, pos);
                //corner 2, down 2, - 1 to side, forward one
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX() + 1, pos.getY() - 2, pos.getZ() + 1),
                        getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.CORNER2), worldIn, pos);
                //Progress, skipped
                //Power, down 1
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()),
                        getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.POWER), worldIn, pos);

                //crystal tank, down 1, + 1 to side
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ()),
                        getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.TANK_CRYSTAL), worldIn, pos);

                //water tank, down 1, - 1 to side
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ()),
                        getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.TANK_WATER), worldIn, pos);

                //crystal input, down 2, + 1 to side
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX() - 1, pos.getY() - 2, pos.getZ()),
                        getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.INPUT_CRYSTAL), worldIn, pos);

                //Water input, down 2, -1 to side
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX() + 1, pos.getY() - 2, pos.getZ()),
                        getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.INPUT_WATER), worldIn, pos);

                //RF input, down 2
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX(), pos.getY() - 2, pos.getZ()),
                        getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.INPUT_RF), worldIn, pos);

                //Output
                setStateWithTileEntity(new net.minecraft.util.math.BlockPos(pos.getX(), pos.getY() - 2, pos.getZ() + 1),
                        getDefaultState().withProperty(PROPERTY_DIRECTION, facing).withProperty(PROPERTY_SECTION, EnumSection.OUTPUT_RESOURCE), worldIn, pos);
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

    private void setStateWithTileEntity(net.minecraft.util.math.BlockPos pos, IBlockState state, World worldIn, BlockPos corePos){
        Boolean setSuccess = worldIn.setBlockState(pos, state);
        TileEntity entity = worldIn.getTileEntity(pos);
        if(entity instanceof TileRefineryComponent){
            ((TileRefineryComponent) entity).setCore((TileRefineryCore)worldIn.getTileEntity(corePos));
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {PROPERTY_DIRECTION, PROPERTY_SECTION});
    }

    /*
    Creating the appropriate TE's are
    We don't know where the rest of the blocks are
     */
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        EnumSection section = (EnumSection)state.getValue(PROPERTY_SECTION);
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
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}