package com.zwstudio.logicpuzzlesandroid.puzzles.abc.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameMove;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class AbcDocument extends GameDocument<AbcGame, AbcGameMove> {

    public void init() {
        super.init("Abc.xml");
        selectedLevelID = gameProgress().levelID;
    }

    public AbcGameProgress gameProgress() {
        try {
            AbcGameProgress rec = app.daoAbcGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new AbcGameProgress();
                app.daoAbcGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AbcLevelProgress levelProgress() {
        try {
            AbcLevelProgress rec = app.daoAbcLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new AbcLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoAbcLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<AbcMoveProgress> moveProgress() {
        try {
            QueryBuilder<AbcMoveProgress, Integer> qb = app.daoAbcMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<AbcMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(AbcGame game) {
        try {
            AbcLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoAbcLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(AbcGame game, AbcGameMove move) {
        try {
            DeleteBuilder<AbcMoveProgress, Integer> deleteBuilder = app.daoAbcMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            AbcMoveProgress rec = new AbcMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.obj = String.valueOf(move.obj);
            app.daoAbcMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            AbcGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoAbcGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<AbcMoveProgress, Integer> deleteBuilder = app.daoAbcMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            AbcLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoAbcLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
