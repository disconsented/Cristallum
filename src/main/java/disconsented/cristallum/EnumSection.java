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

import com.sun.javaws.progress.Progress;
import net.minecraft.util.IStringSerializable;

public enum EnumSection implements IStringSerializable {
    CORNER(0, "CORNER"),
    CORNER2(1, "CORNER2"),
    PROGRESS(2, "PROGRESS"),
    POWER(3, "POWER"),
    TANK_CRYSTAL(4, "TANK_CRYSTAL"),
    TANK_WATER(5, "TANK_WATER"),
    INPUT_CRYSTAL(6, "INPUT_CRYSTAL"),
    INPUT_WATER(7, "INPUT_WATER"),
    INPUT_RF(8, "INPUT_RF"),
    OUTPUT_RESOURCE(9, "OUTPUT_RESOURCE");

    private final int meta;
    private final String name;

    private static final EnumSection[] META_LOOKUP = new EnumSection[values().length];

    private EnumSection(int meta, String name){
        this.meta = meta;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getMetadata() {
        return meta;
    }

    public static EnumSection byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length)
        {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    static
    {
        for (EnumSection value : values()) {
            META_LOOKUP[value.getMetadata()] = value;
        }
    }
}