package com.direwolf20.buildinggadgets2.setup;


import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
    public static final String CLIENT = "client";
    public static final String COMMON = "common";
    public static final String SERVER = "server";

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_POWER = "power";
    public static final String SUBCATEGORY_BUILDINGGADGET = "building_gadget";
    public static final String SUBCATEGORY_EXCHANGINGGADGET = "exchanging_gadget";
    public static final String SUBCATEGORY_CUTPASTEGADGET = "cutpaste_gadget";
    public static final String SUBCATEGORY_COPYPASTEGADGET = "copypaste_gadget";
    public static final String SUBCATEGORY_DESTRUCTIONGADGET = "destruction_gadget";

    public static int BUILDINGGADGET_MAXPOWER;
    public static int BUILDINGGADGET_COST;
    public static int EXCHANGINGGADGET_MAXPOWER;
    public static int EXCHANGINGGADGET_COST;
    public static int CUTPASTEGADGET_MAXPOWER;
    public static int CUTPASTEGADGET_COST;
    public static int COPYPASTEGADGET_MAXPOWER;
    public static int COPYPASTEGADGET_COST;
    public static int DESTRUCTIONGADGET_MAXPOWER;
    public static int DESTRUCTIONGADGET_COST;

    public static int RAYTRACE_RANGE;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        //general
        RAYTRACE_RANGE = configuration.getInt("rayTraceRange", CATEGORY_GENERAL, 32, 1, 64, "Maximum distance you can build at");

        //power
        BUILDINGGADGET_MAXPOWER = configuration.getInt("maxPower", SUBCATEGORY_BUILDINGGADGET, 5_000, 0, Integer.MAX_VALUE, "Maximum power for the Building Gadget");
        BUILDINGGADGET_COST = configuration.getInt("baseCost", SUBCATEGORY_BUILDINGGADGET, 50, 0, Integer.MAX_VALUE, "Base cost per block placed");
        EXCHANGINGGADGET_MAXPOWER = configuration.getInt("maxPower", SUBCATEGORY_EXCHANGINGGADGET, 500_000, 0, Integer.MAX_VALUE, "Maximum power for the Exchanging Gadget");
        EXCHANGINGGADGET_COST = configuration.getInt("baseCost", SUBCATEGORY_EXCHANGINGGADGET, 100, 0, Integer.MAX_VALUE, "Base cost per block exchanged");
        CUTPASTEGADGET_MAXPOWER = configuration.getInt("maxPower", SUBCATEGORY_CUTPASTEGADGET, 5_000_000, 0, Integer.MAX_VALUE, "Maximum power for the Cut and Paste Gadget");
        CUTPASTEGADGET_COST = configuration.getInt("baseCost", SUBCATEGORY_CUTPASTEGADGET, 50, 0, Integer.MAX_VALUE, "Base cost per block - Checked during CUT, Charged during PASTE");
        COPYPASTEGADGET_MAXPOWER = configuration.getInt("maxPower", SUBCATEGORY_COPYPASTEGADGET, 1_000_000, 0, Integer.MAX_VALUE, "Maximum power for the Copy and Paste Gadget");
        COPYPASTEGADGET_COST = configuration.getInt("baseCost", SUBCATEGORY_COPYPASTEGADGET, 50, 0, Integer.MAX_VALUE, "Base cost per block Paste (Copy is Free)");
        DESTRUCTIONGADGET_MAXPOWER = configuration.getInt("maxPower", SUBCATEGORY_DESTRUCTIONGADGET, 1_000_000, 0, Integer.MAX_VALUE, "Maximum power for the Destruction Gadget");
        DESTRUCTIONGADGET_COST = configuration.getInt("baseCost", SUBCATEGORY_DESTRUCTIONGADGET, 200, 0, Integer.MAX_VALUE, "Base cost per block Destroyed");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

}
