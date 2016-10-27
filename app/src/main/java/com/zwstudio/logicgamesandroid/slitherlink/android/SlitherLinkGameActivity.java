package com.zwstudio.logicgamesandroid.slitherlink.android;

import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.TextView;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;
import com.zwstudio.logicgamesandroid.slitherlink.data.SlitherLinkMoveProgress;
import com.zwstudio.logicgamesandroid.slitherlink.domain.SlitherLinkGame;
import com.zwstudio.logicgamesandroid.slitherlink.domain.SlitherLinkGameInterface;
import com.zwstudio.logicgamesandroid.slitherlink.domain.SlitherLinkGameMove;
import com.zwstudio.logicgamesandroid.slitherlink.domain.SlitherLinkGameState;
import com.zwstudio.logicgamesandroid.slitherlink.domain.SlitherLinkObject;
import com.zwstudio.logicgamesandroid.slitherlink.domain.SlitherLinkObjectOrientation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static android.app.AlertDialog.Builder;

@EActivity(R.layout.activity_slitherlink_game)
public class SlitherLinkGameActivity extends SlitherLinkActivity implements SlitherLinkGameInterface {

    @ViewById
    SlitherLinkGameView gameView;
    @ViewById
    TextView tvLevel;
    @ViewById
    TextView tvSolved;
    @ViewById
    TextView tvMoves;

    SlitherLinkGame game;
    boolean levelInitilizing;

    @AfterViews
    void init() {
        startGame();
    }

    @Click
    void btnUndo() {
        game.undo();
    }

    @Click
    void btnRedo() {
        game.redo();
    }

    @Click
    void btnClear() {
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

        Builder builder = new Builder(this);
        builder.setMessage("Do you really want to reset the level?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);

        levelInitilizing = true;
        game = new SlitherLinkGame(layout, this);
        try {
            // restore game state
            for (SlitherLinkMoveProgress rec : doc().moveProgress()) {
                SlitherLinkGameMove move = new SlitherLinkGameMove();
                move.p = new Position(rec.row, rec.col);
                move.objOrientation = SlitherLinkObjectOrientation.values()[rec.objOrientation];
                move.obj = SlitherLinkObject.values()[rec.obj];
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
    public void moveAdded(SlitherLinkGame game, SlitherLinkGameMove move) {
        if (levelInitilizing) return;
        doc().moveAdded(game, move);
    }

    private void updateTextViews(SlitherLinkGame game) {
        tvMoves.setText(String.format("Moves: %d(%d)", game.moveIndex(), game.moveCount()));
        tvSolved.setTextColor(game.isSolved() ? Color.WHITE : Color.BLACK);
    }

    @Override
    public void levelInitilized(SlitherLinkGame game, SlitherLinkGameState state) {
        gameView.invalidate();
        updateTextViews(game);
    }

    @Override
    public void levelUpdated(SlitherLinkGame game, SlitherLinkGameState stateFrom, SlitherLinkGameState stateTo) {
        gameView.invalidate();
        updateTextViews(game);
        if (!levelInitilizing) doc().levelUpdated(game);
    }

    @Override
    public void gameSolved(SlitherLinkGame game) {
        if (!levelInitilizing)
            app().playSoundSolved();
    }
}
