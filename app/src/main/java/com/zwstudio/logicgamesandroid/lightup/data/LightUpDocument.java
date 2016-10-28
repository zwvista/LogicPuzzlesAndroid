package com.zwstudio.logicgamesandroid.lightup.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpGame;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpGameMove;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpGameState;
import com.zwstudio.logicgamesandroid.logicgames.data.DBHelper;
import com.zwstudio.logicgamesandroid.logicgames.data.GameDocument;

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

public class LightUpDocument extends GameDocument<LightUpGame, LightUpGameMove, LightUpGameState> {

    public LightUpDocument(DBHelper db) {
        super(db);
    }

    public LightUpGameProgress gameProgress() {
        try {
            LightUpGameProgress rec = db.getDaoLightUpGameProgress().queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new LightUpGameProgress();
                db.getDaoLightUpGameProgress().create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LightUpLevelProgress levelProgress() {
        try {
            LightUpLevelProgress rec = db.getDaoLightUpLevelProgress().queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new LightUpLevelProgress();
                rec.levelID = selectedLevelID;
                db.getDaoLightUpLevelProgress().create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<LightUpMoveProgress> moveProgress() {
        try {
            QueryBuilder<LightUpMoveProgress, Integer> qb = db.getDaoLightUpMoveProgress().queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<LightUpMoveProgress> rec = qb.query();
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
//Conventional Java
//                        String[] strs = parser.nextText().split("\n");
//                        layout = new ArrayList<>();
//                        for (int i = 2; i < strs.length - 2; i++) {
//                            String s = strs[i];
//                            layout.add(s.substring(0, s.indexOf('\\')));
//                        }
//Java 8
//                        layout = Arrays.asList(parser.nextText().split("\n"));
//                        layout = layout.subList(2, layout.size() - 2)
//                                .stream().map(s -> s.substring(0, s.indexOf('\\')))
//                                .collect(Collectors.toList());
//Functional Java
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

    public void levelUpdated(LightUpGame game) {
        try {
            LightUpLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            db.getDaoLightUpLevelProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(LightUpGame game, LightUpGameMove move) {
        try {
            DeleteBuilder<LightUpMoveProgress, Integer> deleteBuilder = db.getDaoLightUpMoveProgress().deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            LightUpMoveProgress rec = new LightUpMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.objTypeAsString = move.obj.objTypeAsString();
            db.getDaoLightUpMoveProgress().create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            LightUpGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            db.getDaoLightUpGameProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<LightUpMoveProgress, Integer> deleteBuilder = db.getDaoLightUpMoveProgress().deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            LightUpLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            db.getDaoLightUpLevelProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
