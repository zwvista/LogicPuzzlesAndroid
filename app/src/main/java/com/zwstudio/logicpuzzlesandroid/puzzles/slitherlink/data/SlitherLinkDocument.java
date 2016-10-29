package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkGameState;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class SlitherLinkDocument extends GameDocument<SlitherLinkGame, SlitherLinkGameMove, SlitherLinkGameState> {

    public void init() {
        super.init("SlitherLinkLevels.xml");
        selectedLevelID = gameProgress().levelID;
    }

    public SlitherLinkGameProgress gameProgress() {
        try {
            SlitherLinkGameProgress rec = app.daoSlitherLinkGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new SlitherLinkGameProgress();
                app.daoSlitherLinkGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SlitherLinkLevelProgress levelProgress() {
        try {
            SlitherLinkLevelProgress rec = app.daoSlitherLinkLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new SlitherLinkLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoSlitherLinkLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SlitherLinkMoveProgress> moveProgress() {
        try {
            QueryBuilder<SlitherLinkMoveProgress, Integer> qb = app.daoSlitherLinkMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<SlitherLinkMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(SlitherLinkGame game) {
        try {
            SlitherLinkLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoSlitherLinkLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(SlitherLinkGame game, SlitherLinkGameMove move) {
        try {
            DeleteBuilder<SlitherLinkMoveProgress, Integer> deleteBuilder = app.daoSlitherLinkMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            SlitherLinkMoveProgress rec = new SlitherLinkMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.objOrientation = move.objOrientation.ordinal();
            rec.obj = move.obj.ordinal();
            app.daoSlitherLinkMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            SlitherLinkGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoSlitherLinkGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<SlitherLinkMoveProgress, Integer> deleteBuilder = app.daoSlitherLinkMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            SlitherLinkLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoSlitherLinkLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
