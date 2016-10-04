package com.zwstudio.lightupandroid.android;

import android.os.Bundle;

import com.zwstudio.lightupandroid.R;
import com.zwstudio.lightupandroid.data.GameDocument;
import com.zwstudio.lightupandroid.domain.Game;
import com.zwstudio.lightupandroid.domain.GameInterface;
import com.zwstudio.lightupandroid.domain.GameMove;
import com.zwstudio.lightupandroid.domain.GameState;

import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_game)
public class GameActivity extends RoboAppCompatActivity implements GameInterface {

    @InjectView(R.id.gameView)
    GameView gameView;

    Game game;
    GameDocument doc() {return ((GameApplication)getApplicationContext()).getGameDocument();}
    boolean levelInitilizing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startGame();
    }

    private void startGame() {
        List<String> layout = doc().levels.get(doc().selectedLevelID);
        game = new Game(layout, this);
    }

    @Override
    public void moveAdded(Game game, GameMove move) {
        if (levelInitilizing) return;
        doc().moveAdded(game, move);
    }

    @Override
    public void levelInitilized(Game game, GameState state) {
        gameView.invalidate();
    }

    @Override
    public void levelUpdated(Game game, GameState stateFrom, GameState stateTo) {
        gameView.invalidate();
    }

    @Override
    public void gameSolved(Game game) {

    }
}
