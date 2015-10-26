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

import disconsented.cristallum.block.Riparius;
import disconsented.cristallum.obj.OBJLoader;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
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
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        GameRegistry.registerBlock(Riparius.instance, Riparius.name);

        if (event.getSide() == Side.CLIENT)
            clientPreInit();
    }

    private void clientPreInit()
    {
        Item item = Item.getItemFromBlock(Riparius.instance);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.ID.toLowerCase() + ":" + "crystal.obj", "inventory"));
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {

    }


    @Mod.EventHandler
    public void onServerStoppingEvent(FMLServerStoppingEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) throws Exception {

    }
}