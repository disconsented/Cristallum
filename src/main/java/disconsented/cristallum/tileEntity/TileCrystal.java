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
import disconsented.cristallum.potion.PotionCrystalPoison;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.Explosion;

import java.util.Iterator;
import java.util.List;

public class TileCrystal extends TileEntity implements ITickable
{
    public static final String name = "TileCrystal";
    private static final String TAG = "TILECRYSTAL";
    private static final String TAG_CONTAINS = TAG+"_CONTAINS";
    private static final String TAG_TICK = TAG+"TICKS";
    private static final String npeMessage = "Block field cannot be null";
    public Block block = null;
    private int ticks = 0;
    private EnumType enumType;
    private int ticksUntilExplosion = -1;
    //protected ModelResourceLocation model;

    public TileCrystal() {

    }

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
            String string = compound.getString(TAG_CONTAINS);
            //Logging.debug("Reading TileCrystal from NBT with " + string);
            block = Block.getBlockFromName(string);
            ticksUntilExplosion = compound.getInteger(TAG_TICK);
        } catch (Exception e){
            throw e;
        }

        npeCheck();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if(block != null){
            try{
                String string = Block.REGISTRY.getNameForObject(block).toString();
                //Logging.debug("Writing TileCrystal to NBT with " + string);
                compound.setString(TAG_CONTAINS, string);
                compound.setInteger(TAG_TICK, ticksUntilExplosion);
            } catch (Exception e){
                throw e;
            }
        }
        return compound;
    }

    @Override
    public void update() {
        if(!getWorld().isRemote){
            explode();
            //npeCheck();
            if(ticks % 2 == 0){
                ticks = 0;
                final int x = this.pos.getX();
                final int y = this.pos.getY();
                final int z = this.pos.getZ();
                final int radius = 5;
                final net.minecraft.util.math.AxisAlignedBB axisalignedbb = new net.minecraft.util.math.AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
                final List<EntityLivingBase> list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
                final PotionEffect effect = new PotionEffect(PotionCrystalPoison.instance, 6000, 0);
                final int totalToReduce = getTotalToReduce();

                for (EntityLivingBase entity : list)
                {
                    if(!(entity instanceof EntityPlayer  && ((EntityPlayer)entity).capabilities.isCreativeMode)) {

                        int amountLeft = totalToReduce;

                        Iterator<ItemStack> itemStackIterator = entity.getArmorInventoryList().iterator();
                        while (itemStackIterator.hasNext()){
                            ItemStack armour = itemStackIterator.next();
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
                                    //entity.setCurrentItemOrArmor(i+1, null); TODO replace this
                                }

                            }
                        }
                        if (amountLeft > 0) {

                            if (entity != null && !entity.isPotionActive(PotionCrystalPoison.instance) && entity.isEntityAlive() && hasNoArmour(entity))
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
        Iterator<ItemStack> itemStackIterator = entityLivingBase.getArmorInventoryList().iterator();
        while (itemStackIterator.hasNext()){
            return false;
        }
        return true;
    }

    public void setDelayedExplosion() {
        if (ticksUntilExplosion < 0){
            ticksUntilExplosion = Reference.RANDOM.nextInt(400)+200;
        }
    }

    private void explode(){
        if(ticksUntilExplosion != -1){
            if(ticksUntilExplosion == 0){
                Explosion explosion = new Explosion(getWorld(), null, getPos().getX() ,getPos().getY(), getPos().getZ() ,3, true, true);
                explosion.doExplosionA();
                explosion.doExplosionB(true);
                getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
            } else{
                ticksUntilExplosion--;
            }
        }
    }
}

