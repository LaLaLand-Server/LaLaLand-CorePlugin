package de.lalaland.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import de.lalaland.core.user.UserManager
import lombok.Getter
import org.bukkit.plugin.java.JavaPlugin

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */

class CorePlugin : JavaPlugin() {

    @Getter
    var gson: Gson? = null
        private set
    @Getter
    var userManager: UserManager? = null
        private set

    override fun onLoad() {

    }

    override fun onEnable() {
        init()

    }

    override fun onDisable() {

    }

    private fun init() {
        gson = GsonBuilder().setPrettyPrinting().create()
        userManager = UserManager(this)
    }

}