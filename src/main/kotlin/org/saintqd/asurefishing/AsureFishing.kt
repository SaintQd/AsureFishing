package org.saintqd.asurefishing

import org.bukkit.plugin.java.JavaPlugin
import org.saintqd.asurefishing.commands.AsureFishingCommands
import org.saintqd.asurefishing.listeners.PlayerListener
import org.saintqd.asurefishing.managers.FishingManager
import org.saintqd.asurefishing.worldguard.Flags
import org.saintqd.asurelib.AsureLib
import org.saintqd.asurelib.utils.ResourceUtils
import java.io.File

class AsureFishing : JavaPlugin() {

    companion object {
        private var plugin : AsureFishing? = null

        fun inst() : AsureFishing {
            return plugin!!
        }
    }

    override fun onLoad() {
        plugin = this
        Flags.registerFlags()
    }

    override fun onEnable() {
        ResourceUtils.fetchAllResources(this, file)

        loadData()

        AsureFishingCommands.setupCommands(this)

        server.pluginManager.registerEvents(PlayerListener(), this)
    }

    fun loadData() {
        reloadConfig()

        val selectedLang = getConfig().getString("Language")
        val langLines = AsureLib.inst().langManager.loadLanguageFile(
            this,
            dataFolder.path + File.separator + "lang" + File.separator + selectedLang + ".yml"
        )
        AsureLib.inst().langManager.registerLangLines(langLines)

        var prevTime = System.currentTimeMillis()
        FishingManager.instance.loadParams(this)
        var time = System.currentTimeMillis()
        logger.info("Loaded " + FishingManager.instance.fishingTemplates.size + " fishing templates. ("+(time-prevTime)+" ms)");
        prevTime = System.currentTimeMillis()
    }
}