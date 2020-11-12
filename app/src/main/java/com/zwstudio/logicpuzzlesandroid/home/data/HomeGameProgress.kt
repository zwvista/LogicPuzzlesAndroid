package com.zwstudio.logicpuzzlesandroid.home.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class HomeGameProgress : RealmObject() {
    @PrimaryKey
    var id = UUID.randomUUID().toString()
    var gameName = "LightenUp"
    var gameTitle = "Lighten Up"
    var playMusic = true
    var playSound = true
}