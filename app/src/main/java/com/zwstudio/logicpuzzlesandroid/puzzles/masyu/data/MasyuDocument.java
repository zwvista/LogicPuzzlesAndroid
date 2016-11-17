package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameMove;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class MasyuDocument extends GameDocument<MasyuGame, MasyuGameMove> {

    public void init() {
        super.init("Masyu.xml");
        selectedLevelID = gameProgress().levelID;
    }

    public MasyuGameProgress gameProgress() {
        try {
            MasyuGameProgress rec = app.daoMasyuGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new MasyuGameProgress();
                app.daoMasyuGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MasyuLevelProgress levelProgress() {
        try {
            MasyuLevelProgress rec = app.daoMasyuLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new MasyuLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoMasyuLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MasyuMoveProgress> moveProgress() {
        try {
            QueryBuilder<MasyuMoveProgress, Integer> qb = app.daoMasyuMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<MasyuMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(MasyuGame game) {
        try {
            MasyuLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoMasyuLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(MasyuGame game, MasyuGameMove move) {
        try {
            DeleteBuilder<MasyuMoveProgress, Integer> deleteBuilder = app.daoMasyuMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            MasyuMoveProgress rec = new MasyuMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.dir = move.dir;
            app.daoMasyuMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            MasyuGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoMasyuGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<MasyuMoveProgress, Integer> deleteBuilder = app.daoMasyuMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            MasyuLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoMasyuLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
