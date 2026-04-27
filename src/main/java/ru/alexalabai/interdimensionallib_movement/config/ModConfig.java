package ru.alexalabai.interdimensionallib_movement.config;

public class ModConfig {
    public boolean canPlayersSit = true;
    public boolean canPlayersCrawl = true;

    /// FUNCTIONALITY ///
    public static ModConfig INSTANCE = new ModConfig();
    public void save() {ConfigManager.save(this);}
    public static ModConfig load() {return ConfigManager.load();}
}
