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
package disconsented.cristallum.item;

import disconsented.cristallum.Reference;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCrystal extends Item{
    public static final ResourceLocation name =  new ResourceLocation(Reference.ID, "crystalItem");
    public static final String TAG = "CRYSTAL_ITEM_BLOCK";
    //public static final String name = "crystalItem";
    public static ItemCrystal instance = new ItemCrystal();
    private ItemCrystal(){
        setMaxStackSize(64);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName(name.toString());
    }

    public static  void setBlock(Block block, ItemStack itemStack){
        if(block != null && itemStack != null)
        itemStack.getTagCompound().setString(TAG, Block.REGISTRY.getNameForObject(block).toString());
    }

    public static Block getBlock(ItemStack itemStack){
        return Block.REGISTRY.getObject(new ResourceLocation(itemStack.getTagCompound().getString(TAG)));
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
        if (nbtTagCompound != null && nbtTagCompound.hasKey(TAG)){
            tooltip.add("Contents=" + nbtTagCompound.getString(TAG));
        } else {
            tooltip.add("Contents=None");
        }


    }
}