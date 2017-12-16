package com.zwstudio.logicpuzzlesandroid.home.data;

import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

import java.sql.SQLException;

@EBean
public class HomeDocument {
    @App
    public LogicPuzzlesApplication app;

    public HomeGameProgress gameProgress() {
        try {
            HomeGameProgress rec = app.daoHomeGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new HomeGameProgress();
                app.daoHomeGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void resumeGame(String gameName, String gameTitle) {
        try {
            HomeGameProgress rec = gameProgress();
            rec.gameName = gameName;
            rec.gameTitle = gameTitle;
            app.daoHomeGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
