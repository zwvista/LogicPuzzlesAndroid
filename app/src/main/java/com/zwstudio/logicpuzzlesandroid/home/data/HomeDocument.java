package com.zwstudio.logicpuzzlesandroid.home.data;

import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

import java.sql.SQLException;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class HomeDocument {
    @App
    public LogicPuzzlesApplication app;

    public HomeGameProgress gameProgress() {
        try {
            HomeGameProgress rec = app.daoLogicGamesGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new HomeGameProgress();
                app.daoLogicGamesGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void resumeGame(String gameName) {
        try {
            HomeGameProgress rec = gameProgress();
            rec.gameName = gameName;
            app.daoLogicGamesGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
