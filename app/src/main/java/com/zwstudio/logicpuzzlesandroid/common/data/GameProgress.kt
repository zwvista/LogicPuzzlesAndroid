package com.zwstudio.logicpuzzlesandroid.common.data

import com.j256.ormlite.field.DatabaseField
import java.io.Serializable

class GameProgress : Serializable {
    @DatabaseField(generatedId = true)
    private val ID = 0

    @DatabaseField
    var gameID: String? = null

    @DatabaseField
    var levelID = "1"

    @DatabaseField
    var option1: String? = null

    @DatabaseField
    var option2: String? = null

    @DatabaseField
    var option3: String? = null
}