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
package disconsented.cristallum.client;

import disconsented.cristallum.CommonProxy;
import disconsented.cristallum.Reference;
import disconsented.cristallum.Store;
import disconsented.cristallum.block.BlockRefinery;
import disconsented.cristallum.item.ItemCrystal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;

public class ClientProxy extends CommonProxy {

    public static void registerRenderers() {
        OBJLoader.INSTANCE.addDomain(Reference.ID.toLowerCase());
        ModelLoader.setCustomModelResourceLocation(Store.refineryItem, 0, new ModelResourceLocation(BlockRefinery.name, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Store.itemCrystal, 0, new ModelResourceLocation(ItemCrystal.name + "Green", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Store.itemCrystal, 1, new ModelResourceLocation(ItemCrystal.name + "Blue", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Store.itemCrystal, 2, new ModelResourceLocation(ItemCrystal.name + "Red", "inventory"));
    }
}