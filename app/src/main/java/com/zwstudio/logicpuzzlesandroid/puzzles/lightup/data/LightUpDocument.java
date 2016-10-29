package com.zwstudio.logicpuzzlesandroid.puzzles.lightup.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightup.domain.LightUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightup.domain.LightUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightup.domain.LightUpGameState;
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class LightUpDocument extends GameDocument<LightUpGame, LightUpGameMove, LightUpGameState> {

    public void init() {
        super.init("LightUpLevels.xml");
        selectedLevelID = gameProgress().levelID;
    }

    public LightUpGameProgress gameProgress() {
        try {
            LightUpGameProgress rec = app.daoLightUpGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new LightUpGameProgress();
                app.daoLightUpGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LightUpLevelProgress levelProgress() {
        try {
            LightUpLevelProgress rec = app.daoLightUpLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new LightUpLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoLightUpLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<LightUpMoveProgress> moveProgress() {
        try {
            QueryBuilder<LightUpMoveProgress, Integer> qb = app.daoLightUpMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<LightUpMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(LightUpGame game) {
        try {
            LightUpLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoLightUpLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(LightUpGame game, LightUpGameMove move) {
        try {
            DeleteBuilder<LightUpMoveProgress, Integer> deleteBuilder = app.daoLightUpMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            LightUpMoveProgress rec = new LightUpMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.objTypeAsString = move.obj.objTypeAsString();
            app.daoLightUpMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            LightUpGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoLightUpGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<LightUpMoveProgress, Integer> deleteBuilder = app.daoLightUpMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            LightUpLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoLightUpLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
