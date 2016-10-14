package com.zwstudio.logicgamesandroid.lightup.android;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zwstudio.lightupandroid.R;
import com.zwstudio.logicgamesandroid.lightup.data.MoveProgress;
import com.zwstudio.logicgamesandroid.lightup.domain.Game;
import com.zwstudio.logicgamesandroid.lightup.domain.GameInterface;
import com.zwstudio.logicgamesandroid.lightup.domain.GameMove;
import com.zwstudio.logicgamesandroid.lightup.domain.GameObject;
import com.zwstudio.logicgamesandroid.lightup.domain.GameState;
import com.zwstudio.logicgamesandroid.common.Position;

import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static android.app.AlertDialog.Builder;

@ContentView(R.layout.activity_game)
public class GameActivity extends LightUpActivity implements GameInterface {

    @InjectView(R.id.gameView)
    GameView gameView;
    @InjectView(R.id.tvLevel)
    TextView tvLevel;
    @InjectView(R.id.tvSolved)
    TextView tvSolved;
    @InjectView(R.id.tvMoves)
    TextView tvMoves;
    @InjectView(R.id.btnUndo)
    Button btnUndo;
    @InjectView(R.id.btnRedo)
    Button btnRedo;
    @InjectView(R.id.btnClear)
    Button btnClear;

    Game game;
    boolean levelInitilizing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.undo();
            }
        });
        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.redo();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                doc().clearGame();
                                startGame();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                Builder builder = new Builder(GameActivity.this);
                builder.setMessage("Do you really want to reset the level?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        startGame();
    }

    private void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);

        levelInitilizing = true;
        game = new Game(layout, this);
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress())
                game.setObject(new Position(rec.row, rec.col), GameObject.objTypeFromString(rec.objTypeAsString));
            int moveIndex = doc().levelProgress().moveIndex;
            if (!(moveIndex >= 0 && moveIndex < game.moveCount())) return;
            while (moveIndex != game.moveIndex())
                game.undo();
        } finally {
            levelInitilizing = false;
        }
    }

    @Override
    public void moveAdded(Game game, GameMove move) {
        if (levelInitilizing) return;
        doc().moveAdded(game, move);
    }

    private void updateTextViews(Game game) {
        tvMoves.setText(String.format("Moves: %d(%d)", game.moveIndex(), game.moveCount()));
        tvSolved.setTextColor(game.isSolved() ? Color.WHITE : Color.BLACK);
    }

    @Override
    public void levelInitilized(Game game, GameState state) {
        gameView.invalidate();
        updateTextViews(game);
    }

    @Override
    public void levelUpdated(Game game, GameState stateFrom, GameState stateTo) {
        gameView.invalidate();
        updateTextViews(game);
        if (!levelInitilizing) doc().levelUpdated(game);
    }

    @Override
    public void gameSolved(Game game) {
        if (!levelInitilizing)
            app().playSoundSolved();
    }
}
