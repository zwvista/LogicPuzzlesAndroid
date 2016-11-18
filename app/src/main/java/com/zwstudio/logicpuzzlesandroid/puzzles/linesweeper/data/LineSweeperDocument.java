package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameMove;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class LineSweeperDocument extends GameDocument<LineSweeperGame, LineSweeperGameMove> {

    public void init() {
        super.init("LineSweeper.xml");
        selectedLevelID = gameProgress().levelID;
    }

    public LineSweeperGameProgress gameProgress() {
        try {
            LineSweeperGameProgress rec = app.daoLineSweeperGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new LineSweeperGameProgress();
                app.daoLineSweeperGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LineSweeperLevelProgress levelProgress() {
        try {
            LineSweeperLevelProgress rec = app.daoLineSweeperLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new LineSweeperLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoLineSweeperLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<LineSweeperMoveProgress> moveProgress() {
        try {
            QueryBuilder<LineSweeperMoveProgress, Integer> qb = app.daoLineSweeperMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<LineSweeperMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(LineSweeperGame game) {
        try {
            LineSweeperLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoLineSweeperLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(LineSweeperGame game, LineSweeperGameMove move) {
        try {
            DeleteBuilder<LineSweeperMoveProgress, Integer> deleteBuilder = app.daoLineSweeperMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            LineSweeperMoveProgress rec = new LineSweeperMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.objOrientation = move.objOrientation.ordinal();
            rec.obj = move.obj.ordinal();
            app.daoLineSweeperMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            LineSweeperGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoLineSweeperGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<LineSweeperMoveProgress, Integer> deleteBuilder = app.daoLineSweeperMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            LineSweeperLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoLineSweeperLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
