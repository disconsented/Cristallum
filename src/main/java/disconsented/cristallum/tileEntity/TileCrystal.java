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

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ITickable;

import java.util.List;

public class TileCrystal extends TileEntity implements ITickable
{
    public static final String name = "TileCrystal";
    private static final String TAG = "CONTAINED_ORE";
    public Block block = null;
    //protected ModelResourceLocation model;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        Block.getBlockFromName(compound.getString(TAG));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        if(block != null){
            String string = Block.blockRegistry.getNameForObject(block).toString();
            compound.setString(TAG, string);
        }
    }

    public TileCrystal(){

    }


    @Override
    public void update() {
        if(!getWorld().isRemote){
            int x = this.pos.getX();
            int y = this.pos.getY();
            int z = this.pos.getZ();
            int radius = 1;
            AxisAlignedBB axisalignedbb = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
            List<EntityLivingBase> list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
            int effectId = 17;
            PotionEffect effect = new PotionEffect(effectId, 200, 1);

            for (EntityLivingBase entity : list)
            {

                boolean thing = entity.isPotionActive(effectId);
                Object pot =  entity.getActivePotionEffect(Potion.hunger);
                if(!thing){
                    entity.addPotionEffect(new PotionEffect( effect ));
                }
            }
        }
    }
}