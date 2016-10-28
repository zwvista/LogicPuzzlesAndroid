package com.zwstudio.logicgamesandroid.main.data;

import com.zwstudio.logicgamesandroid.main.android.GamesApplication;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

import java.sql.SQLException;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class LogicGamesDocument {
    @App
    public GamesApplication app;

    public LogicGamesGameProgress gameProgress() {
        try {
            LogicGamesGameProgress rec = app.daoLogicGamesGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new LogicGamesGameProgress();
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
            LogicGamesGameProgress rec = gameProgress();
            rec.gameName = gameName;
            app.daoLogicGamesGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
