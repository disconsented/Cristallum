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

import disconsented.cristallum.block.*;
import disconsented.cristallum.client.ClientProxy;
import disconsented.cristallum.item.ItemCrystal;
import disconsented.cristallum.tileEntity.TileCrystal;
import disconsented.cristallum.tileEntity.TileSource;
import disconsented.cristallum.worldGen.WorldGen;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

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
        //TODO: Create items for these blocks

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
        GameRegistry.register(BlockSource.getInstance());
        GameRegistry.registerTileEntity(TileSource.class, BlockSource.name.toString());

        //Refinery

        BlockRefinery blockRefinery = BlockRefinery.getInstance();
        ItemBlock itemBlockRefinery = new ItemBlock(blockRefinery);
        itemBlockRefinery.setRegistryName(blockRefinery.getRegistryName());
        GameRegistry.register(blockRefinery);
        GameRegistry.register(itemBlockRefinery);






        if(event.getSide() == Side.CLIENT){
            ClientProxy.registerRenderers();
        }
        GameRegistry.registerWorldGenerator(new WorldGen(), 2);

        //MinecraftForge.EVENT_BUS.register(BlockCrystal.instance);
    }
}