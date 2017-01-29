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
package disconsented.cristallum;

import disconsented.cristallum.block.BlockCrystal;
import disconsented.cristallum.block.BlockRefinery;
import disconsented.cristallum.block.BlockSource;
import disconsented.cristallum.client.ClientProxy;
import disconsented.cristallum.common.Logging;
import disconsented.cristallum.item.ItemCrystal;
import disconsented.cristallum.potion.PotionCrystalPoison;
import disconsented.cristallum.tileEntity.TileCrystal;
import disconsented.cristallum.tileEntity.TileSource;
import disconsented.cristallum.worldGen.WorldGen;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.StringJoiner;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, acceptableRemoteVersions = "*")
public class Main {
    @Mod.Instance(value = Reference.ID)

    public static Main instance;

    @SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.COMMONPROXY)

    public static CommonProxy proxy;

    public static SimpleNetworkWrapper snw;

    //private final Settings settings = Settings.getInstance();


    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) throws Exception {
        BlockCrystal blockCrystal = BlockCrystal.getInstance();
        ItemBlock itemBlockCrystal = new ItemBlock(blockCrystal);
        itemBlockCrystal.setRegistryName(BlockCrystal.name);
        //Crystal
        GameRegistry.register(blockCrystal);
        GameRegistry.register(itemBlockCrystal);

        //GameRegistry.registerBlock(, BlockCrystal.name);

        GameRegistry.registerTileEntity(TileCrystal.class, BlockCrystal.name.toString());
        GameRegistry.register(ItemCrystal.instance, ItemCrystal.name);

        //Source
        Store.blockSource = new BlockSource();
        GameRegistry.register(Store.blockSource);
        GameRegistry.registerTileEntity(TileSource.class, BlockSource.name.toString());

        //Refinery

        Store.blockRefinery = BlockRefinery.getInstance();
        Store.refineryItem = new ItemBlock(Store.blockRefinery);
        Store.refineryItem.setRegistryName(Store.blockRefinery.getRegistryName());
        GameRegistry.register(Store.blockRefinery);
        GameRegistry.register(Store.refineryItem);

        GameRegistry.addRecipe(new ItemStack(Store.refineryItem), "IDI",
                "GPG",
                "ICI",
                'D', Blocks.DROPPER,
                'I', Items.IRON_INGOT,
                'C', Items.COMPARATOR,
                'G', Blocks.STAINED_GLASS_PANE,
                'P', Blocks.PISTON);
        if(event.getSide() == Side.CLIENT){
            ClientProxy.registerRenderers();
        }
        GameRegistry.registerWorldGenerator(new WorldGen(), 2);

        Store.potionCrystalPoison = new PotionCrystalPoison(true, 1);
        GameRegistry.register(Store.potionCrystalPoison);
        //MinecraftForge.EVENT_BUS.register(BlockCrystal.instance);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        String[] oreDictRaw = OreDictionary.getOreNames();
        for (String string : oreDictRaw) {
            if(string.contains("ore")){
                OreDictionary.getOres(string).forEach(  itemStack -> TileSource.addOre(Block.REGISTRY.getObject(itemStack.getItem().getRegistryName())));
            }
        }
        //TileSource.addOre(Blocks.REDSTONE_ORE);
    }
}