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
import disconsented.cristallum.tileEntity.TileCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public abstract class BlockCrystal extends Block{

    public static final PropertyEnum PROPERTY_ENUM = PropertyEnum.create("type", EnumType.class);
    protected BlockCrystal(Material materialIn) {
        super(materialIn);

        setHardness(1.0F);
        setStepSound(Block.soundTypeGlass);
        setCreativeTab(CreativeTabs.tabMisc);
    }

 /*   @SideOnly(Side.CLIENT)
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

/*    @Override
    public Block setLightLevel(float value) {
        return super.setLightLevel(0);
    }*/

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube() { return false; }


    @Override
    public boolean isVisuallyOpaque() { return true; }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumType enumType = (EnumType)state.getValue(PROPERTY_ENUM);
        return enumType.getMetadata();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileCrystal tileCrystal = (TileCrystal)worldIn.getTileEntity(pos);
        Block block = tileCrystal.block;
        if(block != null) {
            playerIn.addChatMessage(new ChatComponentText(block.getUnlocalizedName()));
            return true;
        }
            return false;

    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumType type = EnumType.byMetadata(meta);
        return getDefaultState().withProperty(PROPERTY_ENUM,type);
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {PROPERTY_ENUM});
    }
}