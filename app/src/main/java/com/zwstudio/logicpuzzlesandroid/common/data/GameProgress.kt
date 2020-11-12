package com.zwstudio.logicpuzzlesandroid.common.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class GameProgress : RealmObject() {
    @PrimaryKey
    var id = UUID.randomUUID().toString()
    var gameID: String? = null
    var levelID = "1"
    var option1: String? = null
    var option2: String? = null
    var option3: String? = null
}