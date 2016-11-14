package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGameMove;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class SkyscrapersDocument extends GameDocument<SkyscrapersGame, SkyscrapersGameMove> {

    public void init() {
        super.init("Skyscrapers.xml");
        selectedLevelID = gameProgress().levelID;
    }

    public SkyscrapersGameProgress gameProgress() {
        try {
            SkyscrapersGameProgress rec = app.daoSkyscrapersGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new SkyscrapersGameProgress();
                app.daoSkyscrapersGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SkyscrapersLevelProgress levelProgress() {
        try {
            SkyscrapersLevelProgress rec = app.daoSkyscrapersLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new SkyscrapersLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoSkyscrapersLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SkyscrapersMoveProgress> moveProgress() {
        try {
            QueryBuilder<SkyscrapersMoveProgress, Integer> qb = app.daoSkyscrapersMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<SkyscrapersMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(SkyscrapersGame game) {
        try {
            SkyscrapersLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoSkyscrapersLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(SkyscrapersGame game, SkyscrapersGameMove move) {
        try {
            DeleteBuilder<SkyscrapersMoveProgress, Integer> deleteBuilder = app.daoSkyscrapersMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            SkyscrapersMoveProgress rec = new SkyscrapersMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.obj = String.valueOf(move.obj);
            app.daoSkyscrapersMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            SkyscrapersGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoSkyscrapersGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<SkyscrapersMoveProgress, Integer> deleteBuilder = app.daoSkyscrapersMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            SkyscrapersLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoSkyscrapersLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
