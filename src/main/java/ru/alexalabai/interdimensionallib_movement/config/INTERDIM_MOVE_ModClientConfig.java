package ru.alexalabai.interdimensionallib_movement.config;

import ru.alexalabai.interdimensionallib_movement.common.INTERDIM_MOVE_ClientGameOptions;

public class INTERDIM_MOVE_ModClientConfig {
    public boolean crawlToggleable=true;
    public boolean sitOnBlockClick=false;

    /// FUNCTIONALITY ///
    public static INTERDIM_MOVE_ModClientConfig INSTANCE = new INTERDIM_MOVE_ModClientConfig();
    public void save() {
        crawlToggleable=INTERDIM_MOVE_ClientGameOptions.crawlToggle.getValue();
        sitOnBlockClick=INTERDIM_MOVE_ClientGameOptions.sitToggle.getValue();
        INTERDIM_MOVE_ConfigManager.saveClient(this);
    }
    public static INTERDIM_MOVE_ModClientConfig load() {return INTERDIM_MOVE_ConfigManager.loadClient();}
}
