package com.zwstudio.logicgamesandroid.bridges.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGame;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGameMove;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGameState;
import com.zwstudio.logicgamesandroid.logicgames.android.GameApplication;
import com.zwstudio.logicgamesandroid.logicgames.data.GameDocument;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BridgesDocument extends GameDocument<BridgesGame, BridgesGameMove, BridgesGameState> {

    public BridgesDocument(GameApplication app, String filename) {
        super(app, filename);
        selectedLevelID = gameProgress().levelID;
    }

    public BridgesGameProgress gameProgress() {
        try {
            BridgesGameProgress rec = app.daoBridgesGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new BridgesGameProgress();
                app.daoBridgesGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BridgesLevelProgress levelProgress() {
        try {
            BridgesLevelProgress rec = app.daoBridgesLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new BridgesLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoBridgesLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BridgesMoveProgress> moveProgress() {
        try {
            QueryBuilder<BridgesMoveProgress, Integer> qb = app.daoBridgesMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<BridgesMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(BridgesGame game) {
        try {
            BridgesLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoBridgesLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(BridgesGame game, BridgesGameMove move) {
        try {
            DeleteBuilder<BridgesMoveProgress, Integer> deleteBuilder = app.daoBridgesMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            BridgesMoveProgress rec = new BridgesMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.rowFrom = move.pFrom.row;
            rec.colFrom = move.pFrom.col;
            rec.rowTo = move.pTo.row;
            rec.colTo = move.pTo.col;
            app.daoBridgesMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            BridgesGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoBridgesGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<BridgesMoveProgress, Integer> deleteBuilder = app.daoBridgesMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            BridgesLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoBridgesLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
