package com.zwstudio.logicgamesandroid.clouds.android;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.clouds.data.CloudsMoveProgress;
import com.zwstudio.logicgamesandroid.clouds.domain.CloudsGame;
import com.zwstudio.logicgamesandroid.clouds.domain.CloudsGameInterface;
import com.zwstudio.logicgamesandroid.clouds.domain.CloudsGameMove;
import com.zwstudio.logicgamesandroid.clouds.domain.CloudsGameState;
import com.zwstudio.logicgamesandroid.clouds.domain.CloudsObject;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static android.app.AlertDialog.Builder;

@EActivity(R.layout.activity_clouds_game)
public class CloudsGameActivity extends CloudsActivity implements CloudsGameInterface {

    @ViewById
    CloudsGameView gameView;
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

    CloudsGame game;
    boolean levelInitilizing;

    @AfterViews
    protected void init() {

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

                Builder builder = new Builder(CloudsGameActivity.this);
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
        game = new CloudsGame(layout, this);
        try {
            // restore game state
            for (CloudsMoveProgress rec : doc().moveProgress()) {
                CloudsGameMove move = new CloudsGameMove();
                move.p = new Position(rec.row, rec.col);
                move.obj = CloudsObject.values()[rec.obj];
                game.setObject(move);
            }
            int moveIndex = doc().levelProgress().moveIndex;
            if (!(moveIndex >= 0 && moveIndex < game.moveCount())) return;
            while (moveIndex != game.moveIndex())
                game.undo();
        } finally {
            levelInitilizing = false;
        }
    }

    @Override
    public void moveAdded(CloudsGame game, CloudsGameMove move) {
        if (levelInitilizing) return;
        doc().moveAdded(game, move);
    }

    private void updateTextViews(CloudsGame game) {
        tvMoves.setText(String.format("Moves: %d(%d)", game.moveIndex(), game.moveCount()));
        tvSolved.setTextColor(game.isSolved() ? Color.WHITE : Color.BLACK);
    }

    @Override
    public void levelInitilized(CloudsGame game, CloudsGameState state) {
        gameView.invalidate();
        updateTextViews(game);
    }

    @Override
    public void levelUpdated(CloudsGame game, CloudsGameState stateFrom, CloudsGameState stateTo) {
        gameView.invalidate();
        updateTextViews(game);
        if (!levelInitilizing) doc().levelUpdated(game);
    }

    @Override
    public void gameSolved(CloudsGame game) {
        if (!levelInitilizing)
            app().playSoundSolved();
    }
}
