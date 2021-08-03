package com.zwstudio.logicpuzzlesandroid.home.data

import com.zwstudio.logicpuzzlesandroid.home.android.realm

class HomeDocument {
    fun gameProgress(): HomeGameProgress {
        var rec = realm.where(HomeGameProgress::class.java).findFirst()
        if (rec == null) {
            realm.beginTransaction()
            rec = HomeGameProgress()
            realm.insertOrUpdate(rec)
            realm.commitTransaction()
        }
        return rec
    }

    fun resumeGame(gameName: String, gameTitle: String) {
        realm.beginTransaction()
        val rec = gameProgress()
        rec.gameName = gameName
        rec.gameTitle = gameTitle
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
    }
}