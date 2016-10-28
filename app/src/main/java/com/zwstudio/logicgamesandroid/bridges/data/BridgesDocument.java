package com.zwstudio.logicgamesandroid.bridges.data;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGame;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGameMove;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGameState;
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

public class BridgesDocument extends GameDocument<BridgesGame, BridgesGameMove, BridgesGameState> {

    public BridgesDocument(DBHelper db, Context context, String filename) {
        super(db, context, filename);
        selectedLevelID = gameProgress().levelID;
    }

    public BridgesGameProgress gameProgress() {
        try {
            BridgesGameProgress rec = db.getDaoBridgesGameProgress().queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new BridgesGameProgress();
                db.getDaoBridgesGameProgress().create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BridgesLevelProgress levelProgress() {
        try {
            BridgesLevelProgress rec = db.getDaoBridgesLevelProgress().queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new BridgesLevelProgress();
                rec.levelID = selectedLevelID;
                db.getDaoBridgesLevelProgress().create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BridgesMoveProgress> moveProgress() {
        try {
            QueryBuilder<BridgesMoveProgress, Integer> qb = db.getDaoBridgesMoveProgress().queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<BridgesMoveProgress> rec = qb.query();
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

    public void levelUpdated(BridgesGame game) {
        try {
            BridgesLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            db.getDaoBridgesLevelProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(BridgesGame game, BridgesGameMove move) {
        try {
            DeleteBuilder<BridgesMoveProgress, Integer> deleteBuilder = db.getDaoBridgesMoveProgress().deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            BridgesMoveProgress rec = new BridgesMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.rowFrom = move.pFrom.row;
            rec.colFrom = move.pFrom.col;
            rec.rowTo = move.pTo.row;
            rec.colTo = move.pTo.col;
            db.getDaoBridgesMoveProgress().create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            BridgesGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            db.getDaoBridgesGameProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<BridgesMoveProgress, Integer> deleteBuilder = db.getDaoBridgesMoveProgress().deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            BridgesLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            db.getDaoBridgesLevelProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
