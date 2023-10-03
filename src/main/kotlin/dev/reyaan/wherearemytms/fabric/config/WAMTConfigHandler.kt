package dev.reyaan.wherearemytms.fabric.config

import com.google.gson.GsonBuilder
import java.io.File
import java.io.IOException
import java.nio.file.Files

class WAMTConfigHandler(
    private val file: File,
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun init(): WAMTConfigObject {
        if (!file.exists()) {
            deserialize()
            return WAMTConfigObject()
        }
        return serialize()
    }

    fun serialize(): WAMTConfigObject {
        return try {
            gson.fromJson(
                Files.readString(file.toPath()),
                WAMTConfigObject::class.java
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun deserialize() {
        try {
            Files.writeString(file.toPath(), gson.toJson(WAMTConfigObject()))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}