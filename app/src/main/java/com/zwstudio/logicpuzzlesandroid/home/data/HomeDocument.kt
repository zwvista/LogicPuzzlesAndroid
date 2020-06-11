package com.zwstudio.logicpuzzlesandroid.home.data

import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EBean
import java.sql.SQLException

@EBean
open class HomeDocument {
    @App
    lateinit var app: LogicPuzzlesApplication
    fun gameProgress(): HomeGameProgress? {
        return try {
            var rec = app.daoHomeGameProgress.queryBuilder()
                .queryForFirst()
            if (rec == null) {
                rec = HomeGameProgress()
                app.daoHomeGameProgress.create(rec)
            }
            rec
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    fun resumeGame(gameName: String, gameTitle: String) {
        try {
            val rec = gameProgress()!!
            rec.gameName = gameName
            rec.gameTitle = gameTitle
            app.daoHomeGameProgress.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}