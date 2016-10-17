package com.zwstudio.logicgamesandroid.logicgames.data;

import java.sql.SQLException;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LogicGamesDocument {

    public String selectedGameName;
    public DBHelper db;

    public LogicGamesDocument(DBHelper db) {
        this.db = db;
    }

    public LogicGamesGameProgress gameProgress() {
        try {
            LogicGamesGameProgress rec = db.getDaoLogicGamesGameProgress().queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new LogicGamesGameProgress();
                db.getDaoLogicGamesGameProgress().create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void resumeGame() {
        try {
            LogicGamesGameProgress rec = gameProgress();
            rec.gameName = selectedGameName;
            db.getDaoLogicGamesGameProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
