package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameMove;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class LightenUpDocument extends GameDocument<LightenUpGame, LightenUpGameMove> {

    public void init() {
        super.init("LightenUp.xml");
        selectedLevelID = gameProgress().levelID;
    }

    public LightenUpGameProgress gameProgress() {
        try {
            LightenUpGameProgress rec = app.daoLightenUpGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new LightenUpGameProgress();
                app.daoLightenUpGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LightenUpLevelProgress levelProgress() {
        try {
            LightenUpLevelProgress rec = app.daoLightenUpLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new LightenUpLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoLightenUpLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<LightenUpMoveProgress> moveProgress() {
        try {
            QueryBuilder<LightenUpMoveProgress, Integer> qb = app.daoLightenUpMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<LightenUpMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(LightenUpGame game) {
        try {
            LightenUpLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoLightenUpLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(LightenUpGame game, LightenUpGameMove move) {
        try {
            DeleteBuilder<LightenUpMoveProgress, Integer> deleteBuilder = app.daoLightenUpMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            LightenUpMoveProgress rec = new LightenUpMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.objTypeAsString = move.obj.objTypeAsString();
            app.daoLightenUpMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            LightenUpGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoLightenUpGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<LightenUpMoveProgress, Integer> deleteBuilder = app.daoLightenUpMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            LightenUpLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoLightenUpLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
