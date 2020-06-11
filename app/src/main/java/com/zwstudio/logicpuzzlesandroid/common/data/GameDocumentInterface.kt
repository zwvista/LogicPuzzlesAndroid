package com.zwstudio.logicpuzzlesandroid.common.data

interface GameDocumentInterface {
    val markerOption: Int
    fun setMarkerOption(rec: GameProgress, o: Int)
    val isAllowedObjectsOnly: Boolean
    fun setAllowedObjectsOnly(rec: GameProgress, o: Boolean)
}