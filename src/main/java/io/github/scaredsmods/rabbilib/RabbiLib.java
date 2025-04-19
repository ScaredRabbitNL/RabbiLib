package io.github.scaredsmods.rabbilib;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(RabbiLib.MOD_ID)
public class RabbiLib  {


    public static final String MOD_ID = "rabbilib";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RabbiLib (IEventBus bus, ModContainer container) {


    }

}
