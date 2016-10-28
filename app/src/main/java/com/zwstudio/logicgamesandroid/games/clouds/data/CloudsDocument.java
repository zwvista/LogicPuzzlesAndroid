package com.zwstudio.logicgamesandroid.games.clouds.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicgamesandroid.games.clouds.domain.CloudsGame;
import com.zwstudio.logicgamesandroid.games.clouds.domain.CloudsGameMove;
import com.zwstudio.logicgamesandroid.games.clouds.domain.CloudsGameState;
import com.zwstudio.logicgamesandroid.common.data.GameDocument;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class CloudsDocument extends GameDocument<CloudsGame, CloudsGameMove, CloudsGameState> {

    public void init() {
        super.init("CloudsLevels.xml");
        selectedLevelID = gameProgress().levelID;
    }

    public CloudsGameProgress gameProgress() {
        try {
            CloudsGameProgress rec = app.daoCloudsGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new CloudsGameProgress();
                app.daoCloudsGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CloudsLevelProgress levelProgress() {
        try {
            CloudsLevelProgress rec = app.daoCloudsLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new CloudsLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoCloudsLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CloudsMoveProgress> moveProgress() {
        try {
            QueryBuilder<CloudsMoveProgress, Integer> qb = app.daoCloudsMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<CloudsMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(CloudsGame game) {
        try {
            CloudsLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoCloudsLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(CloudsGame game, CloudsGameMove move) {
        try {
            DeleteBuilder<CloudsMoveProgress, Integer> deleteBuilder = app.daoCloudsMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            CloudsMoveProgress rec = new CloudsMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.obj = move.obj.ordinal();
            app.daoCloudsMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            CloudsGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoCloudsGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<CloudsMoveProgress, Integer> deleteBuilder = app.daoCloudsMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            CloudsLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoCloudsLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
