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
import disconsented.cristallum.common.Logging;
import disconsented.cristallum.potion.PotionCrystalPoison;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;

import java.util.List;

public class TileCrystal extends TileEntity implements ITickable
{
    public static final String name = "TileCrystal";
    private static final String TAG = "CONTAINED_ORE";
    public Block block = null;
    private int ticks = 0;
    private EnumType enumType;

    private static final String npeMessage = "Block field cannot be null";
    //protected ModelResourceLocation model;

    /**
     * For when we want to crash the game.
     */
    private void npeCheck(){
        if(block == null)
            throw new NullPointerException(npeMessage);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        try{
            String string = compound.getString(TAG);
            Logging.debug("Reading TileCrystal from NBT with " + string);
            block = Block.getBlockFromName(string);
        } catch (Exception e){
            throw e;
        }

        npeCheck();
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        npeCheck();
        try{
            String string = Block.blockRegistry.getNameForObject(block).toString();
            Logging.debug("Writing TileCrystal to NBT with " + string);
            compound.setString(TAG, string);
        } catch (Exception e){
            throw e;
        }

    }

    public TileCrystal(){

    }


    @Override
    public void update() {
        if(!getWorld().isRemote){
            npeCheck();
            if(ticks % 2 == 0){
                ticks = 0;
                final int x = this.pos.getX();
                final int y = this.pos.getY();
                final int z = this.pos.getZ();
                final int radius = 5;
                final AxisAlignedBB axisalignedbb = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
                final List<EntityLivingBase> list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
                final int effectId = PotionCrystalPoison.instance.getId();
                final PotionEffect effect = new PotionEffect(effectId, 6000, 0);
                final int totalToReduce = getTotalToReduce();

                for (EntityLivingBase entity : list)
                {
                    if(!(entity instanceof EntityPlayer  && ((EntityPlayer)entity).capabilities.isCreativeMode)) {

                        int amountLeft = totalToReduce;

                        for (int i = 0; i < 4; i++) {
                            ItemStack armour = entity.getCurrentArmor(i);
                            if (armour != null) {
                                int remainingDurability = armour.getMaxDamage() - armour.getItemDamage();
                                if (amountLeft > remainingDurability) {
                                    amountLeft = amountLeft - remainingDurability;
                                    armour.attemptDamageItem(remainingDurability, Reference.RANDOM);
                                    //armour.damageItem(remainingDurability, entity);
                                } else {
                                    armour.attemptDamageItem(amountLeft, Reference.RANDOM);
                                    //armour.damageItem(amountLeft, entity);
                                    //entity.attackEntityFrom(DamageSource.magic,.01f);
                                    break;
                                }
                                if(armour.getMaxDamage() - armour.getItemDamage() == 0){
                                    entity.setCurrentItemOrArmor(i+1, null);
                                }

                            }
                        }
                        if (amountLeft > 0) {

                            if (!entity.isPotionActive(effectId) && entity.isEntityAlive() && hasNoArmour(entity))
                                entity.addPotionEffect(new PotionEffect(effect));
                        }
                    }
                }
            } else {
                ticks++;
            }
        }
    }
    private int getTotalToReduce(){
        if(enumType == null){
            enumType = (EnumType) getWorld().getBlockState(getPos()).getValue(BlockCrystal.PROPERTY_ENUM);
        }
        return enumType.getArmourDamage();
    }

    /**
     *
     * @param entityLivingBase The entity being checked against.
     * @return True if the entity is not wearing any armour.
     */
    private boolean hasNoArmour(EntityLivingBase entityLivingBase){
        for (int i = 0; i < 4; i++) {
            if(entityLivingBase.getCurrentArmor(i) != null)
                return false;
        }
        return true;
    }
}

