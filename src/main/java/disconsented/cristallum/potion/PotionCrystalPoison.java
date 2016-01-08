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
package disconsented.cristallum.potion;

import disconsented.cristallum.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionCrystalPoison extends Potion{

    private static final String name = "CrystalToxemia";
    public static final PotionCrystalPoison instance = new PotionCrystalPoison(new ResourceLocation(Reference.ID,name), true, 1);

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_) {
        int duration = entityLivingBaseIn.getActivePotionEffect(instance).getDuration();
        if(duration == 1){
            entityLivingBaseIn.setHealth(0f);
        }
    }

    @Override
    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        return true;
    }

    protected PotionCrystalPoison(ResourceLocation location, boolean badEffect, int potionColor) {
        super(location, badEffect, potionColor);
    }

    @Override
    public Potion setPotionName(String nameIn) {
        return super.setPotionName(name);
    }

    @Override
    public String getName() {
        return name;
    }
}