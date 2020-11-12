package com.zwstudio.logicpuzzlesandroid.common.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class LevelProgress : RealmObject() {
    @PrimaryKey
    var id = UUID.randomUUID().toString()
    var gameID: String? = null
    var levelID: String? = null
    var moveIndex = 0
}