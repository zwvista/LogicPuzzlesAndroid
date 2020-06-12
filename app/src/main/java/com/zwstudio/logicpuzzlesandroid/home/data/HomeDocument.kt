package com.zwstudio.logicpuzzlesandroid.home.data

import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EBean

@EBean
open class HomeDocument {
    @App
    lateinit var app: LogicPuzzlesApplication
    fun gameProgress(): HomeGameProgress {
        var rec = app.daoHomeGameProgress.queryBuilder()
            .queryForFirst()
        if (rec == null) {
            rec = HomeGameProgress()
            app.daoHomeGameProgress.create(rec)
        }
        return rec
    }

    fun resumeGame(gameName: String, gameTitle: String) {
        val rec = gameProgress()
        rec.gameName = gameName
        rec.gameTitle = gameTitle
        app.daoHomeGameProgress.update(rec)
    }
}