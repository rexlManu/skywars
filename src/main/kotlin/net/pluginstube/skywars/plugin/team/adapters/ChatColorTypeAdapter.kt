package net.pluginstube.skywars.plugin.team.adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.bukkit.ChatColor

class ChatColorTypeAdapter : TypeAdapter<ChatColor>() {
    override fun write(out: JsonWriter?, value: ChatColor?) {
        if (value == null) {
            out?.nullValue()
            return
        }
        out?.value(value.name.lowercase())
    }

    override fun read(`in`: JsonReader?): ChatColor {
        if (`in`?.peek() == JsonToken.NULL) return ChatColor.BLACK

        return ChatColor.valueOf(`in`!!.nextString().uppercase())
    }
}