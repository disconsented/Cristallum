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

import disconsented.cristallum.EnumType;
import disconsented.cristallum.Reference;
import disconsented.cristallum.item.ItemCrystal;
import disconsented.cristallum.tileEntity.TileCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCrystal extends Block{

    public static final PropertyEnum PROPERTY_ENUM = PropertyEnum.create("type", EnumType.class);
    public static BlockCrystal instance;
    public static final ResourceLocation name =  new ResourceLocation(Reference.ID, "crystal");
    private static final int searchRadius = 1;
    protected BlockCrystal(Material materialIn) {
        super(materialIn);
        setHardness(1.0F);
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName(Reference.ID + ":" + name);

    }

    public BlockCrystal(){
        super(Material.GROUND);
        setRegistryName(name);
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {

        for (int x = pos.getX() - searchRadius; x < pos.getX() + searchRadius; x++) {
            for (int y = pos.getY() - searchRadius; y < pos.getY() + searchRadius; y++) {
                for (int z = pos.getZ() - searchRadius; z < pos.getZ() + searchRadius; z++) {
                    IBlockState blockState = world.getBlockState(pos);
                    EnumType type = (EnumType) blockState.getValue(BlockCrystal.PROPERTY_ENUM);
                    if(blockState.getBlock() == BlockCrystal.instance && type == EnumType.ABOREUS){
                        TileEntity tileEntity = world.getTileEntity(new net.minecraft.util.math.BlockPos(x, y, z));
                        if(tileEntity != null && tileEntity instanceof TileCrystal) {
                            TileCrystal tileCrystal = (TileCrystal) tileEntity;
                            tileCrystal.setDelayedExplosion();
                        }
                    }
                }
            }
        }
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }


    @Override
    public boolean isVisuallyOpaque() { return true; }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumType enumType = (EnumType)state.getValue(PROPERTY_ENUM);
        return enumType.getMetadata();
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if(!world.isRemote){
            TileEntity entity = world.getTileEntity(pos);
            if (entity instanceof TileCrystal) {
                ItemStack itemStack = new ItemStack(ItemCrystal.instance, 1, ((EnumType) state.getValue(PROPERTY_ENUM)).getMetadata());
                itemStack.setTagCompound(new NBTTagCompound());
                Block block = ((TileCrystal) entity).block;
                ItemCrystal.setBlock(block, itemStack);
                EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                world.spawnEntityInWorld(entityItem);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote){
            TileCrystal tileCrystal = (TileCrystal)worldIn.getTileEntity(pos);
            Block block = tileCrystal.block;
            if(block != null) {
                playerIn.addChatMessage(new TextComponentString(block.getUnlocalizedName()));
                return true;
            }
        }
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumType type = EnumType.byMetadata(meta);
        return getDefaultState().withProperty(PROPERTY_ENUM,type);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {PROPERTY_ENUM});
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCrystal();
    }
}