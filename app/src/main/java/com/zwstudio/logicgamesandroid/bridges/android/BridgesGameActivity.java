package com.zwstudio.logicgamesandroid.bridges.android;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.bridges.data.BridgesMoveProgress;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGame;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGameInterface;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGameMove;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGameState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static android.app.AlertDialog.Builder;

@EActivity(R.layout.activity_bridges_game)
public class BridgesGameActivity extends BridgesActivity implements BridgesGameInterface {

    @ViewById
    BridgesGameView gameView;
    @ViewById
    TextView tvLevel;
    @ViewById
    TextView tvSolved;
    @ViewById
    TextView tvMoves;
    @ViewById
    Button btnUndo;
    @ViewById
    Button btnRedo;
    @ViewById
    Button btnClear;

    BridgesGame game;
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

                Builder builder = new Builder(BridgesGameActivity.this);
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
        game = new BridgesGame(layout, this);
        try {
            // restore game state
            for (BridgesMoveProgress rec : doc().moveProgress())
                game.switchBridges(new Position(rec.rowFrom, rec.colFrom), new Position(rec.rowTo, rec.colTo));
            int moveIndex = doc().levelProgress().moveIndex;
            if (!(moveIndex >= 0 && moveIndex < game.moveCount())) return;
            while (moveIndex != game.moveIndex())
                game.undo();
        } finally {
            levelInitilizing = false;
        }
    }

    @Override
    public void moveAdded(BridgesGame game, BridgesGameMove move) {
        if (levelInitilizing) return;
        doc().moveAdded(game, move);
    }

    private void updateTextViews(BridgesGame game) {
        tvMoves.setText(String.format("Moves: %d(%d)", game.moveIndex(), game.moveCount()));
        tvSolved.setTextColor(game.isSolved() ? Color.WHITE : Color.BLACK);
    }

    @Override
    public void levelInitilized(BridgesGame game, BridgesGameState state) {
        gameView.invalidate();
        updateTextViews(game);
    }

    @Override
    public void levelUpdated(BridgesGame game, BridgesGameState stateFrom, BridgesGameState stateTo) {
        gameView.invalidate();
        updateTextViews(game);
        if (!levelInitilizing) doc().levelUpdated(game);
    }

    @Override
    public void gameSolved(BridgesGame game) {
        if (!levelInitilizing)
            app().playSoundSolved();
    }
}
