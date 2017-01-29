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
import disconsented.cristallum.Store;
import disconsented.cristallum.block.BlockCrystal;
import disconsented.cristallum.potion.PotionCrystalPoison;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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

public class TileCrystal extends TileEntity implements ITickable {
    public static final String name = "TileCrystal";
    private static final String TAG = "TILECRYSTAL";
    private static final String TAG_CONTAINS = TAG + "_CONTAINS";
    private static final String TAG_TICK = TAG + "TICKS";
    private static final String TAG_ARMOUR = TAG + "TRUEARMOUR";
    private static final String npeMessage = "Block field cannot be null";
    public Block block = null;
    private EnumType enumType;
    private int ticksUntilExplosion = -1;
    //The value we use for
    private int armourMultipler = 20;
    //protected ModelResourceLocation model;

    public TileCrystal() {

    }

    /**
     * For when we want to crash the game.
     */
    private void npeCheck() {
        if (block == null)
            throw new NullPointerException(npeMessage);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        try {
            String string = compound.getString(TAG_CONTAINS);
            //Logging.debug("Reading TileCrystal from NBT with " + string);
            block = Block.getBlockFromName(string);
            ticksUntilExplosion = compound.getInteger(TAG_TICK);
        } catch (Exception e) {
            throw e;
        }

//        npeCheck();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (block != null) {
            try {
                String string = Block.REGISTRY.getNameForObject(block).toString();
                //Logging.debug("Writing TileCrystal to NBT with " + string);
                compound.setString(TAG_CONTAINS, string);
                compound.setInteger(TAG_TICK, ticksUntilExplosion);
            } catch (Exception e) {
                throw e;
            }
        }
        return compound;
    }

    @Override
    public void update() {
        if (getWorld().getBlockState(getPos()).getBlock() == BlockCrystal.getInstance()) {//Just doublec check that we have the right block on both sides
            if (!getWorld().isRemote) {//Only want this to happen server side. Otherwise things will get confused
                explode();
            }
            final int x = this.pos.getX();
            final int y = this.pos.getY();
            final int z = this.pos.getZ();
            final int radius = 5;
            final net.minecraft.util.math.AxisAlignedBB axisalignedbb = new net.minecraft.util.math.AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
            final List<EntityLivingBase> list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
            final PotionEffect effect = new PotionEffect(Store.potionCrystalPoison, 6000, 0);
            final int totalToReduce = getTotalToReduce();

            for (EntityLivingBase entity : list) {
                if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)) {//Looking for a not creative

                    int amountLeft = totalToReduce;

                    Iterator<ItemStack> itemStackIterator = entity.getArmorInventoryList().iterator();
                    while (itemStackIterator.hasNext() && amountLeft > 0) {
                        ItemStack armour = itemStackIterator.next();
                        if (armour != null) {//Checking there actually is armour, remember to change this to isEmpty() whenever that change happens
                            //Get the true armour valce
                            NBTTagCompound compound = armour.getSubCompound(Reference.ID, true);
                            int trueArmour = compound.getInteger(TAG_ARMOUR);
                            //If it doesnt exist then create it
                            if (trueArmour == 0) {
                                trueArmour = armour.getMaxDamage() * armourMultipler;
                                compound.setInteger(TAG_ARMOUR, trueArmour);
                            }
                            int remainingDurability = trueArmour - amountLeft;
                            if (amountLeft > remainingDurability) {//The armour can't take all the damage
                                amountLeft = amountLeft - remainingDurability;
                                armour.setItemDamage(armour.getMaxDamage() + 1);

                            } else { // The armour can take it all
                                trueArmour -= amountLeft;
                                int itemDamage = armour.getMaxDamage() - trueArmour / armourMultipler;
                                armour.setItemDamage(itemDamage);//Damage in minecraft... The higher the number the less duri
                                amountLeft = 0;
                                compound.setInteger(TAG_ARMOUR, trueArmour);
                                break;
                            }
                        }
                    }
                }
                if (!entity.isPotionActive(Store.potionCrystalPoison) && entity.isEntityAlive() && hasNoArmour(entity))
                    if(!(entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode))//If its a player make sure its not in creative
                    entity.addPotionEffect(new PotionEffect(effect));
            }
        }
    }

    private int getTotalToReduce() {
        if (enumType == null) {
            IBlockState state = getWorld().getBlockState(getPos());
            if (state != null) {
                enumType = state.getValue(BlockCrystal.PROPERTY_ENUM);
            }

        }
        return enumType.getArmourDamage();
    }

    /**
     * @param entityLivingBase The entity being checked against.
     * @return True if the entity is not wearing any armour.
     */
    private boolean hasNoArmour(EntityLivingBase entityLivingBase) {
        Iterator<ItemStack> itemStackIterator = entityLivingBase.getArmorInventoryList().iterator();
        while (itemStackIterator.hasNext()) {
            ItemStack itemStack = itemStackIterator.next();
            if (itemStack != null && itemStack.getItemDamage() < itemStack.getMaxDamage()) {
                return false;
            }
        }
        return true;
    }

    public void setDelayedExplosion() {
        if (ticksUntilExplosion < 0) {
            ticksUntilExplosion = Reference.RANDOM.nextInt(400) + 200;
        }
    }

    private void explode() {
        if (ticksUntilExplosion != -1) {
            if (ticksUntilExplosion == 0) {
                Explosion explosion = new Explosion(getWorld(), null, getPos().getX(), getPos().getY(), getPos().getZ(), 3, true, true);
                explosion.doExplosionA();
                explosion.doExplosionB(true);
                getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
            } else {
                ticksUntilExplosion--;
            }
        }
    }
}

