package com.zwstudio.logicgamesandroid.logicgames.data;

import java.sql.SQLException;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LogicGamesDocument {

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

    public static String getGameName(Class<?> cls) {
        String str = cls.getSimpleName();
        return str.substring(0, str.length() - "MainActivity".length());
    }

    public void resumeGame(Class<?> cls) {
        try {
            LogicGamesGameProgress rec = gameProgress();
            rec.gameName = getGameName(cls);
            db.getDaoLogicGamesGameProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
