package ru.alexalabai.interdimensionallib_movement.config;

public class INTERDIM_MOVE_ModConfig {
    public boolean canPlayersSit = true;
    public boolean canPlayersCrawl = true;

    /// FUNCTIONALITY ///
    public static INTERDIM_MOVE_ModConfig INSTANCE = new INTERDIM_MOVE_ModConfig();
    public void save() {
        INTERDIM_MOVE_ConfigManager.save(this);}
    public static INTERDIM_MOVE_ModConfig load() {return INTERDIM_MOVE_ConfigManager.load();}
}
