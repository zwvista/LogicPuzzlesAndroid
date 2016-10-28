package com.zwstudio.logicgamesandroid.logicgames.data;

import com.zwstudio.logicgamesandroid.logicgames.domain.Game;
import com.zwstudio.logicgamesandroid.logicgames.domain.GameState;

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

    public GameDocument(DBHelper db) {
        this.db = db;
    }

    public abstract void levelUpdated(G game);

    public abstract void moveAdded(G game, GM move);

    public abstract void clearGame();
}
