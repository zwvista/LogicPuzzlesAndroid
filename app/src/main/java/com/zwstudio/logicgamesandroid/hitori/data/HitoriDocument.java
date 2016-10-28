package com.zwstudio.logicgamesandroid.hitori.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicgamesandroid.hitori.domain.HitoriGame;
import com.zwstudio.logicgamesandroid.hitori.domain.HitoriGameMove;
import com.zwstudio.logicgamesandroid.hitori.domain.HitoriGameState;
import com.zwstudio.logicgamesandroid.logicgames.data.GameDocument;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class HitoriDocument extends GameDocument<HitoriGame, HitoriGameMove, HitoriGameState> {

    public void init() {
        super.init("HitoriLevels.xml");
        selectedLevelID = gameProgress().levelID;
    }

    public HitoriGameProgress gameProgress() {
        try {
            HitoriGameProgress rec = app.daoHitoriGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new HitoriGameProgress();
                app.daoHitoriGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HitoriLevelProgress levelProgress() {
        try {
            HitoriLevelProgress rec = app.daoHitoriLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new HitoriLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoHitoriLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<HitoriMoveProgress> moveProgress() {
        try {
            QueryBuilder<HitoriMoveProgress, Integer> qb = app.daoHitoriMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<HitoriMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(HitoriGame game) {
        try {
            HitoriLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoHitoriLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(HitoriGame game, HitoriGameMove move) {
        try {
            DeleteBuilder<HitoriMoveProgress, Integer> deleteBuilder = app.daoHitoriMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            HitoriMoveProgress rec = new HitoriMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.obj = move.obj.ordinal();
            app.daoHitoriMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            HitoriGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoHitoriGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<HitoriMoveProgress, Integer> deleteBuilder = app.daoHitoriMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            HitoriLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoHitoriLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
