package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameMove;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class NurikabeDocument extends GameDocument<NurikabeGame, NurikabeGameMove> {

    public void init() {
        super.init("NurikabeLevels.xml");
        selectedLevelID = gameProgress().levelID;
    }

    public NurikabeGameProgress gameProgress() {
        try {
            NurikabeGameProgress rec = app.daoNurikabeGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new NurikabeGameProgress();
                app.daoNurikabeGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public NurikabeLevelProgress levelProgress() {
        try {
            NurikabeLevelProgress rec = app.daoNurikabeLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new NurikabeLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoNurikabeLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<NurikabeMoveProgress> moveProgress() {
        try {
            QueryBuilder<NurikabeMoveProgress, Integer> qb = app.daoNurikabeMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<NurikabeMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(NurikabeGame game) {
        try {
            NurikabeLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoNurikabeLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(NurikabeGame game, NurikabeGameMove move) {
        try {
            DeleteBuilder<NurikabeMoveProgress, Integer> deleteBuilder = app.daoNurikabeMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            NurikabeMoveProgress rec = new NurikabeMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.objAsString = move.obj.objAsString();
            app.daoNurikabeMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            NurikabeGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoNurikabeGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<NurikabeMoveProgress, Integer> deleteBuilder = app.daoNurikabeMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            NurikabeLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoNurikabeLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
