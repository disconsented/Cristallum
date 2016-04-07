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
import disconsented.cristallum.tileEntity.TileSource;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlockSource extends Block implements ITileEntityProvider {
    public static final PropertyEnum PROPERTY_ENUM = PropertyEnum.create("type", EnumType.class);
    public static final BlockSource instance = new BlockSource();
    //public static final String name = "source";
    public static final ResourceLocation name =  new ResourceLocation(Reference.ID, "source");
    //private  AxisAlignedBB boundingBox;
    protected BlockSource() {
        super(Material.iron);
        setCreativeTab(CreativeTabs.tabMisc);
        setUnlocalizedName(Reference.ID + ":" + name);
    }

    @Override
    public float getExplosionResistance(Entity exploder) {
        return 2f;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return true; }

    @Override
    public boolean isFullCube(IBlockState state) { return true; }

    /*private AxisAlignedBB getBoundingBox(BlockPos pos){
        if(boundingBox == null){
            boundingBox = new AxisAlignedBB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+3.7,pos.getZ()+1);
        }
        return boundingBox;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        list.add(getBoundingBox(pos));
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return getBoundingBox(pos);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return getBoundingBox(pos);
    }*/

    @Override
    public void onBlockAdded(World worldIn, net.minecraft.util.math.BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        int rng = Reference.RANDOM.nextInt(3);
        EnumType enumType = EnumType.byMetadata(rng);

        IBlockState outState = state.withProperty(BlockCrystal.PROPERTY_ENUM, enumType);

        worldIn.setBlockState(pos, outState);



        TileSource crystal = (TileSource) worldIn.getTileEntity(pos);
        crystal.scan();
    }

    @Override
    public boolean isVisuallyOpaque() { return true; }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileSource();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumType enumType = (EnumType)state.getValue(PROPERTY_ENUM);
        return enumType.getMetadata();
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
}