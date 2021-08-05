package net.pluginstube.skywars.plugin.team.adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.bukkit.DyeColor

class DyeColorTypeAdapter : TypeAdapter<DyeColor>() {
    override fun write(out: JsonWriter?, value: DyeColor?) {
        if (value == null) {
            out?.nullValue()
            return
        }
        out?.value(value.name.lowercase())
    }

    override fun read(`in`: JsonReader?): DyeColor {
        if (`in`?.peek() == JsonToken.NULL) return DyeColor.BLACK

        return DyeColor.valueOf(`in`!!.nextString().uppercase())
    }
}