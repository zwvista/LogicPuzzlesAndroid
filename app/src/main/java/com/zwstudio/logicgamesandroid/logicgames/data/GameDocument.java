package com.zwstudio.logicgamesandroid.logicgames.data;

import android.content.Context;

import com.zwstudio.logicgamesandroid.logicgames.domain.Game;
import com.zwstudio.logicgamesandroid.logicgames.domain.GameState;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class GameDocument<G extends Game, GM, GS extends GameState> {

    public Map<String, List<String>> levels = new HashMap<>();
    public String selectedLevelID;
    public DBHelper db;

    public GameDocument(DBHelper db, Context context, String filename) {
        this.db = db;
        try {
            InputStream in_s = context.getAssets().open(filename);
            loadXml(in_s);
            in_s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    protected abstract void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException;

    public abstract void levelUpdated(G game);

    public abstract void moveAdded(G game, GM move);

    public abstract void clearGame();
}
