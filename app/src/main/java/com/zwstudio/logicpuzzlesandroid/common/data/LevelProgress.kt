package com.zwstudio.logicpuzzlesandroid.common.data

import com.j256.ormlite.field.DatabaseField
import java.io.Serializable

class LevelProgress : Serializable {
    @DatabaseField(generatedId = true)
    private val ID = 0

    @DatabaseField
    var gameID: String? = null

    @DatabaseField
    var levelID: String? = null

    @DatabaseField
    var moveIndex = 0
}