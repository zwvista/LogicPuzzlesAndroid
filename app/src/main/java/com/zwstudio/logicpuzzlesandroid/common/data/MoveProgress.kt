package com.zwstudio.logicpuzzlesandroid.common.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class MoveProgress : RealmObject() {
    @PrimaryKey
    var id = UUID.randomUUID().toString()
    var gameID: String? = null
    var levelID: String? = null
    var moveIndex = 0
    var row = 0
    var col = 0
    var row2 = 0
    var col2 = 0
    var intValue1 = 0
    var intValue2 = 0
    var intValue3 = 0
    var strValue1: String? = null
    var strValue2: String? = null
    var strValue3: String? = null
}