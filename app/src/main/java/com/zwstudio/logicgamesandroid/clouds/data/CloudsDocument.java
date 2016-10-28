package com.zwstudio.logicgamesandroid.clouds.data;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicgamesandroid.clouds.domain.CloudsGame;
import com.zwstudio.logicgamesandroid.clouds.domain.CloudsGameMove;
import com.zwstudio.logicgamesandroid.clouds.domain.CloudsGameState;
import com.zwstudio.logicgamesandroid.logicgames.data.DBHelper;
import com.zwstudio.logicgamesandroid.logicgames.data.GameDocument;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class CloudsDocument extends GameDocument<CloudsGame, CloudsGameMove, CloudsGameState> {

    public CloudsDocument(DBHelper db, Context context, String filename) {
        super(db, context, filename);
        selectedLevelID = gameProgress().levelID;
    }

    public CloudsGameProgress gameProgress() {
        try {
            CloudsGameProgress rec = db.getDaoCloudsGameProgress().queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new CloudsGameProgress();
                db.getDaoCloudsGameProgress().create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CloudsLevelProgress levelProgress() {
        try {
            CloudsLevelProgress rec = db.getDaoCloudsLevelProgress().queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new CloudsLevelProgress();
                rec.levelID = selectedLevelID;
                db.getDaoCloudsLevelProgress().create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CloudsMoveProgress> moveProgress() {
        try {
            QueryBuilder<CloudsMoveProgress, Integer> qb = db.getDaoCloudsMoveProgress().queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<CloudsMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            String id = null;
            List<String> layout = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("level")){
                        id = "Level " + parser.getAttributeValue(null,"id");
                        layout = Arrays.asList(parser.nextText().split("\n"));
                        layout = iterableList(layout.subList(2, layout.size() - 2))
                                .map(s -> s.substring(0, s.indexOf('\\')))
                                .toJavaList();
                        levels.put(id, layout);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.next();
        }

    }

    public void levelUpdated(CloudsGame game) {
        try {
            CloudsLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            db.getDaoCloudsLevelProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(CloudsGame game, CloudsGameMove move) {
        try {
            DeleteBuilder<CloudsMoveProgress, Integer> deleteBuilder = db.getDaoCloudsMoveProgress().deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            CloudsMoveProgress rec = new CloudsMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.obj = move.obj.ordinal();
            db.getDaoCloudsMoveProgress().create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            CloudsGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            db.getDaoCloudsGameProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<CloudsMoveProgress, Integer> deleteBuilder = db.getDaoCloudsMoveProgress().deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            CloudsLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            db.getDaoCloudsLevelProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
