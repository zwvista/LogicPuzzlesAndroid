package com.zwstudio.logicgamesandroid.logicgames.data;

import com.zwstudio.logicgamesandroid.logicgames.android.GameApplication;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

import java.sql.SQLException;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class LogicGamesDocument {
    @App
    public GameApplication app;

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
