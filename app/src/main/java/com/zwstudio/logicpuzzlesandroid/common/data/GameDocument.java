package com.zwstudio.logicpuzzlesandroid.common.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicpuzzlesandroid.common.domain.Game;
import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
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

@EBean
public abstract class GameDocument<G extends Game, GM> {
    public String gameID() {
        String name = getClass().getSimpleName();
        return name.substring(0, name.indexOf("Document"));
    }

    public Map<String, List<String>> levels = new HashMap<>();
    public String selectedLevelID;
    @App
    public LogicPuzzlesApplication app;

    public void init() {
        String filename = gameID() + ".xml";
        try {
            InputStream in_s = app.getApplicationContext().getAssets().open(filename);
            loadXml(in_s);
            in_s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectedLevelID = gameProgress().levelID;
    }

    private void loadXml(InputStream in_s) {
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

    public GameProgress gameProgress() {
        try {
            GameProgress rec = app.daoGameProgress.queryBuilder()
                    .where().eq("gameID", gameID())
                    .queryForFirst();
            if (rec == null) {
                rec = new GameProgress();
                rec.gameID = gameID();
                app.daoGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LevelProgress levelProgress() {
        try {
            LevelProgress rec = app.daoLevelProgress.queryBuilder()
                    .where().eq("gameID", gameID())
                    .and().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new LevelProgress();
                rec.gameID = gameID();
                rec.levelID = selectedLevelID;
                app.daoLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MoveProgress> moveProgress() {
        try {
            QueryBuilder<MoveProgress, Integer> builder = app.daoMoveProgress.queryBuilder();
            builder.where().eq("gameID", gameID())
                    .and().eq("levelID", selectedLevelID);
            builder.orderBy("moveIndex", true);
            List<MoveProgress> rec = builder.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(Game game) {
        try {
            LevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(Game game, GM move) {
        try {
            DeleteBuilder<MoveProgress, Integer> builder = app.daoMoveProgress.deleteBuilder();
            builder.where().eq("gameID", gameID())
                    .and().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            builder.delete();
            MoveProgress rec = new MoveProgress();
            rec.gameID = gameID();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            saveMove(move, rec);
            app.daoMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected abstract void saveMove(GM move, MoveProgress rec);
    public abstract GM loadMove(MoveProgress rec);

    public void resumeGame() {
        try {
            GameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<MoveProgress, Integer> builder = app.daoMoveProgress.deleteBuilder();
            builder.where().eq("gameID", gameID())
                    .and().eq("levelID", selectedLevelID);
            builder.delete();
            LevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
