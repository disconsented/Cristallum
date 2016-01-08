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

import net.minecraft.util.IStringSerializable;

public enum EnumType implements IStringSerializable {
    RIPARIUS(0, "RIPARIUS", 200, .3, 1),
    VINIFERA(1, "VINIFERA", 400, 0, 3),
    ABOREUS(2, "ABOREUS", 800, -.3, 5);

    private final int meta;
    private final String name;
    private final int tickRate;
    private final double weight;
    private final int armourDamage;
    private static final EnumType[] META_LOOKUP = new EnumType[values().length];

    private EnumType(int meta, String name, int tickRate, double weight, int armourDamage){
        this.meta = meta;
        this.name = name;
        this.tickRate = tickRate;
        this.weight = weight;
        this.armourDamage = armourDamage;
    }

    public int getArmourDamage(){
        return armourDamage;
    }

    public double getWeight(){
        return weight;
    }

    public int getTickRate(){
        return tickRate;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getMetadata() {
        return meta;
    }

    public static EnumType byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length)
        {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    static
    {
        for (EnumType value : values()) {
            META_LOOKUP[value.getMetadata()] = value;
        }
    }
}