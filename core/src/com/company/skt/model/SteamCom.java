package com.company.skt.model;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;

public class SteamCom {
    
    public static void test() {
        try {
            SteamAPI.loadLibraries();
            if (!SteamAPI.init()) {
                // Steamworks initialization error, e.g. Steam client not running
            }
        } catch (SteamException e) {
            // Error extracting or loading native libraries
        }
    }
    
}
