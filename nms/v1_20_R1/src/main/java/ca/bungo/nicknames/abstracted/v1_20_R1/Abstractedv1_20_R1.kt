package ca.bungo.nicknames.abstracted.v1_20_R1

import ca.bungo.nicknames.abstracted.AbstractedHandler
import ca.bungo.nicknames.abstracted.AbstractedLink
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.server.level.ServerPlayer
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.NoSuchElementException

class Abstractedv1_20_R1(helper: AbstractedLink) : AbstractedHandler(helper) {

    var playerRecord: MutableMap<String, String> = HashMap()

    override fun hidePlayerName(player: Player?) {
        val sPlayer: ServerPlayer = (player as CraftPlayer).handle

        val playerGP = sPlayer.gameProfile

        val newProfile: GameProfile = GameProfile(player.uniqueId, " ")

        try {
            val skinProp = playerGP.properties.get("textures").iterator().next()
            newProfile.properties.put("textures", Property("textures", skinProp.value, skinProp.signature))
        } catch(e: NoSuchElementException) {
            player.sendMessage("Failed to save skin. You might need to load your Skin URL again!")
        }

        if(playerRecord[player.uniqueId.toString()] == null)
            playerRecord[player.uniqueId.toString()] = player.name

        sPlayer.gameProfile = newProfile

        for(otherPlayer in Bukkit.getOnlinePlayers()){
            if(otherPlayer == player) continue
            otherPlayer.hidePlayer(helper.instance, player)
            otherPlayer.showPlayer(helper.instance, player)
        }
    }

    override fun showPlayerName(player: Player?) {
        val sPlayer: ServerPlayer = (player as CraftPlayer).handle

        if(playerRecord[player.uniqueId.toString()] == null) return

        val playerGP = sPlayer.gameProfile
        val newProfile: GameProfile = GameProfile(player.uniqueId, playerRecord[player.uniqueId.toString()])

        try {
            val skinProp = playerGP.properties.get("textures").iterator().next()
            newProfile.properties.put("textures", Property("textures", skinProp.value, skinProp.signature))
        } catch(e: NoSuchElementException) {
            player.sendMessage("Failed to save skin. You might need to load your Skin URL again!")
        }

        sPlayer.gameProfile = newProfile

        playerRecord.remove(player.uniqueId.toString())
        for(otherPlayer in Bukkit.getOnlinePlayers()){
            if(otherPlayer == player) continue
            otherPlayer.hidePlayer(helper.instance, player)
            otherPlayer.showPlayer(helper.instance, player)
        }
    }


}