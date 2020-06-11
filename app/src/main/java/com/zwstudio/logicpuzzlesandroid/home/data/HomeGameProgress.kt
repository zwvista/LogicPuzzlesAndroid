package com.zwstudio.logicpuzzlesandroid.home.data

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.io.Serializable

@DatabaseTable
class HomeGameProgress : Serializable {
    @DatabaseField(generatedId = true)
    private val ID = 0

    @DatabaseField
    var gameName = "LightenUp"

    @DatabaseField
    var gameTitle = "Lighten Up"

    @DatabaseField
    var playMusic = true

    @DatabaseField
    var playSound = true
}