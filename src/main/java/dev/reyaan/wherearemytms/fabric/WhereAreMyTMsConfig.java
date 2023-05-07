package dev.reyaan.wherearemytms.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class WhereAreMyTMsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

//    boolean allow_egg_moves = false;
    boolean allow_tutor_moves = false;

    public static WhereAreMyTMsConfig fromFile(File configReadFile) {
        try {
            return GSON.fromJson(Files.readString(configReadFile.toPath()), WhereAreMyTMsConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void toFile(File configFile) {
        try {
            Files.writeString(configFile.toPath(), GSON.toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}