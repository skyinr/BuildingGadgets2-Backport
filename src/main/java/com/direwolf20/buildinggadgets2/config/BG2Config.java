package com.direwolf20.buildinggadgets2.config;


import com.direwolf20.buildinggadgets2.BuildingGadgets2;
import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = BuildingGadgets2.MODID, filename = "buildinggadget2-common")
public class BG2Config {

    @Config.DefaultInt(32)
    @Config.Comment("Maximum distance you can build at")
    @Config.RangeInt(min = 1, max = 64)
    public static int RAYTRACE_RANGE;

    @Config.DefaultInt(5_000)
    @Config.RangeInt(min = 0)
    @Config.Comment("Maximum power for the Building Gadget")
    public static int BUILDINGGADGET_MAXPOWER;

    @Config.DefaultInt(50)
    @Config.RangeInt(min = 0)
    @Config.Comment("Base cost per block placed")
    public static int BUILDINGGADGET_COST;

    @Config.DefaultInt(500_000)
    @Config.RangeInt(min = 0)
    @Config.Comment("Maximum power for the Exchanging Gadget")
    public static int EXCHANGINGGADGET_MAXPOWER;

    @Config.DefaultInt(100)
    @Config.RangeInt(min = 0)
    @Config.Comment("Base cost per block exchanged")
    public static int EXCHANGINGGADGET_COST;

    @Config.DefaultInt(5_000_000)
    @Config.RangeInt(min = 0)
    @Config.Comment("Maximum power for the Cut and Paste Gadget")
    public static int CUTPASTEGADGET_MAXPOWER;

    @Config.DefaultInt(50)
    @Config.RangeInt(min = 0)
    @Config.Comment("Base cost per block - Checked during CUT, Charged during PASTE")
    public static int CUTPASTEGADGET_COST;

    @Config.DefaultInt(1_000_000)
    @Config.RangeInt(min = 0)
    @Config.Comment("Maximum power for the Copy and Paste Gadget")
    public static int COPYPASTEGADGET_MAXPOWER;

    @Config.DefaultInt(50)
    @Config.RangeInt(min = 0)
    @Config.Comment("Base cost per block Paste (Copy is Free)")
    public static int COPYPASTEGADGET_COST;

    @Config.DefaultInt(1_000_000)
    @Config.RangeInt(min = 0)
    @Config.Comment("Maximum power for the Destruction Gadget")
    public static int DESTRUCTIONGADGET_MAXPOWER;

    @Config.DefaultInt(200)
    @Config.RangeInt(min = 0)
    @Config.Comment("Base cost per block Destroyed")
    public static int DESTRUCTIONGADGET_COST;

}
