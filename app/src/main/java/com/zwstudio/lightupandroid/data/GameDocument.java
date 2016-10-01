package com.zwstudio.lightupandroid.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.zwstudio.lightupandroid.domain.Game;
import com.zwstudio.lightupandroid.domain.GameMove;

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
import java.util.stream.Collectors;

/**
 * Created by zwvista on 2016/09/29.
 */

public class GameDocument {

    public Map<String, List<String>> levels = new HashMap<>();
    public String selectedLevelID;
    private DBHelper db;

    public GameDocument(DBHelper db) {
        this.db = db;
    }

    public GameProgress gameProgeress() {
        try {
            GameProgress rec = db.getDaoGameProgress().queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new GameProgress();
                rec.levelID = "Level 1";
                db.getDaoGameProgress().create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LevelProgress levelProgeress() {
        try {
            LevelProgress rec = db.getDaoLevelProgress().queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new LevelProgress();
                rec.levelID = selectedLevelID;
                db.getDaoLevelProgress().create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MoveProgress> moveProgeress() {
        try {
            List<MoveProgress> rec = db.getDaoMoveProgress().queryBuilder()
                    .where().eq("levelID", selectedLevelID).query();
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
                        id = parser.getAttributeValue(null,"id");
                        layout = Arrays.asList(parser.nextText().split("\n"));
                        layout = layout.subList(2, layout.size() - 2)
                                .stream().map(s -> s.substring(0, s.length() - 1))
                                .collect(Collectors.toList());
                        levels.put(id, layout);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.next();
        }

    }

    public void levelUpdated(Game game) {
        try {
            LevelProgress rec = levelProgeress();
            rec.moveIndex = game.moveIndex();
            db.getDaoLevelProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(Game game, GameMove move) {
        try {
            DeleteBuilder<MoveProgress, Integer> deleteBuilder = db.getDaoMoveProgress().deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().gt("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            MoveProgress rec = new MoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.objTypeAsString = move.obj.objTypeAsString();
            db.getDaoMoveProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            GameProgress rec = gameProgeress();
            rec.levelID = selectedLevelID;
            db.getDaoGameProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<MoveProgress, Integer> deleteBuilder = db.getDaoMoveProgress().deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            LevelProgress rec = levelProgeress();
            rec.moveIndex = 0;
            db.getDaoLevelProgress().update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
