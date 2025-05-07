package com.example.archmod;

import net.fabricmc.api.ModInitializer;

public class ArchMod implements ModInitializer {
    @Override
    public void onInitialize() {
        PacmanCommand.register();
    }
}