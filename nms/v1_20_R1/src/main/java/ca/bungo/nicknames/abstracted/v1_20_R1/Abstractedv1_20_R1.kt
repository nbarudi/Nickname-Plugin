package ca.bungo.nicknames.abstracted.v1_20_R1

import ca.bungo.nicknames.abstracted.AbstractedHandler
import ca.bungo.nicknames.abstracted.AbstractedLink
import ca.bungo.nicknames.abstracted.INametag
import com.mojang.math.Transformation
import io.papermc.paper.adventure.PaperAdventure
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.util.Brightness
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Display.TextDisplay
import net.minecraft.world.entity.EntityType
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.joml.Quaternionf
import org.joml.Vector3f

class Abstractedv1_20_R1(helper: AbstractedLink) : AbstractedHandler(helper) {

    class LocalNametag(val player: Player) : INametag {
        private var mounted: TextDisplay
        private var nmsPlayer = (player as CraftPlayer).handle

        init {
            mounted = TextDisplay(EntityType.TEXT_DISPLAY, nmsPlayer.level())
            mounted.setPos(nmsPlayer.x, nmsPlayer.y, nmsPlayer.z)

            mounted.billboardConstraints = Display.BillboardConstraints.CENTER
            mounted.lineWidth = 150
            var flagBits: Byte = mounted.flags
            flagBits = (flagBits.toInt() or 2).toByte()
            flagBits = (flagBits.toInt() or 4).toByte()

            mounted.flags = flagBits
            mounted.brightnessOverride = Brightness(15, 15)
            mounted.interpolationDuration = 0
            mounted.interpolationDelay = -1
            mounted.setTransformation(Transformation(Vector3f(0F, 0.7F, 0F), Quaternionf(), null, null))

            player.addPassenger(mounted.bukkitEntity)
            for(target in Bukkit.getOnlinePlayers())
                spawn(target)
        }

        override fun setName(name: net.kyori.adventure.text.Component) {
            mounted.text = PaperAdventure.asVanilla(name)
            val dataPacket = ClientboundSetEntityDataPacket(mounted.id, mounted.entityData.nonDefaultValues!!)
            for(target in Bukkit.getOnlinePlayers()){
                val nmsTarget = (target as CraftPlayer).handle
                nmsTarget.connection.send(dataPacket)
            }
        }

        override fun setLocalizedName(name: String, target: Player) {
            val ogComp = mounted.text
            mounted.text = Component.literal(name)
            val dataPacket = ClientboundSetEntityDataPacket(mounted.id, mounted.entityData.nonDefaultValues!!)
            val nmsTarget = (target as CraftPlayer).handle
            nmsTarget.connection.send(dataPacket)
            mounted.text = ogComp
        }

        override fun setLocalizedName(name: net.kyori.adventure.text.Component?, target: Player) {
            val ogComp = mounted.text
            mounted.text = PaperAdventure.asVanilla(name)
            val dataPacket = ClientboundSetEntityDataPacket(mounted.id, mounted.entityData.nonDefaultValues!!)
            val nmsTarget = (target as CraftPlayer).handle
            nmsTarget.connection.send(dataPacket)
            mounted.text = ogComp
        }

        override fun getName(): String {
            return mounted.text.string
        }

        override fun destroy() {
            val removePacket = ClientboundRemoveEntitiesPacket(mounted.id)
            for(target in Bukkit.getOnlinePlayers()){
                val nmsTarget = (target as CraftPlayer).handle
                nmsTarget.connection.send(removePacket)
            }
        }

        override fun spawn(target: Player) {
            val nmsTarget = (target as CraftPlayer).handle
            val addPacket = ClientboundAddEntityPacket(mounted)
            val dataPacket = ClientboundSetEntityDataPacket(mounted.id, mounted.entityData.nonDefaultValues!!)
            nmsTarget.connection.send(addPacket)
            nmsTarget.connection.send(dataPacket)
        }

    }

    override fun createNametag(player: Player): INametag {
        return LocalNametag(player)
    }

}