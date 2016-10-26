package com.zwstudio.logicgamesandroid.hitori.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicgamesandroid.hitori.domain.HitoriGame;
import com.zwstudio.logicgamesandroid.hitori.domain.HitoriGameMove;
import com.zwstudio.logicgamesandroid.logicgames.data.DBHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class HitoriDocument {

    public Map<String, List<String>> levels = new HashMap<>();
    public String selectedLevelID;
    public DBHelper db;

    public HitoriDocument(DBHelper db) {
        this.db = db;
    }

    public HitoriGameProgress gameProgress() {
        try {
            HitoriGameProgress rec = db.getDaoHitoriGameProgress().queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new HitoriGameProgress();
                db.getDaoHitoriGameProgress().create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HitoriLevelProgress levelProgress() {
        try {
            HitoriLevelProgress rec = db.getDaoHitoriLevelProgress().queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new HitoriLevelProgress();
                rec.levelID = selectedLevelID;
                db.getDaoHitoriLevelProgress().create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<HitoriMoveProgress> moveProgress() {
        try {
            QueryBuilder<HitoriMoveProgress, Integer> qb = db.getDaoHitoriMoveProgress().queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<HitoriMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadXml(InputStream in_s) {
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser);

            selectedLevelID = gameProgress().levelID;

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
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

    public void levelUpdated(HitoriGame game) {
        try {
            HitoriLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            db.getDaoHitoriLevelProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(HitoriGame game, HitoriGameMove move) {
        try {
            DeleteBuilder<HitoriMoveProgress, Integer> deleteBuilder = db.getDaoHitoriMoveProgress().deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            HitoriMoveProgress rec = new HitoriMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.obj = move.obj.ordinal();
            db.getDaoHitoriMoveProgress().create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            HitoriGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            db.getDaoHitoriGameProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<HitoriMoveProgress, Integer> deleteBuilder = db.getDaoHitoriMoveProgress().deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            HitoriLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            db.getDaoHitoriLevelProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
