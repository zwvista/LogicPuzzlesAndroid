package com.zwstudio.lightupandroid.data;

import java.sql.SQLException;
import java.util.List;

public class RepoMoveProgress {

    DBHelper db;

    public RepoMoveProgress(DBHelper db) {
        this.db = db;
    }

    public List<GameProgress> getData() {
        try {
            return db.getDaoGameProgress().queryBuilder()
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
