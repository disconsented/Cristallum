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
import disconsented.cristallum.Reference;
import disconsented.cristallum.item.ItemCrystal;
import disconsented.cristallum.tileEntity.TileRefinery;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRefinery extends Block {

    public static BlockRefinery instance;

    public static final ResourceLocation name =  new ResourceLocation(Reference.ID, "refinery");

    public BlockRefinery(String name){
        super(Material.anvil);
        this.setDefaultState(this.blockState.getBaseState());
        setCreativeTab(CreativeTabs.tabMisc);
        setUnlocalizedName(Reference.ID + ":" + this.name);
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            ItemStack inUse = playerIn.getHeldItemMainhand();
            if(inUse != null && inUse.getItem() instanceof ItemCrystal){
                playerIn.inventory.setInventorySlotContents(playerIn.inventory.getSlotFor(inUse), null);
                worldIn.spawnEntityInWorld(
                        new EntityItem(worldIn, pos.getX(),pos.getY()+1,pos.getZ(),
                                new ItemStack(ItemCrystal.getBlock(inUse))));
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

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileRefinery();
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