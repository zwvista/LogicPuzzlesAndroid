package com.zwstudio.logicpuzzlesandroid.common.data

import com.j256.ormlite.field.DatabaseField
import java.io.Serializable

class MoveProgress : Serializable {
    @DatabaseField(generatedId = true)
    private val ID = 0

    @DatabaseField
    var gameID: String? = null

    @DatabaseField
    var levelID: String? = null

    @DatabaseField
    var moveIndex = 0

    @DatabaseField
    var row = 0

    @DatabaseField
    var col = 0

    @DatabaseField
    var row2 = 0

    @DatabaseField
    var col2 = 0

    @DatabaseField
    var intValue1 = 0

    @DatabaseField
    var intValue2 = 0

    @DatabaseField
    var intValue3 = 0

    @DatabaseField
    var strValue1: String? = null

    @DatabaseField
    var strValue2: String? = null

    @DatabaseField
    var strValue3: String? = null
}