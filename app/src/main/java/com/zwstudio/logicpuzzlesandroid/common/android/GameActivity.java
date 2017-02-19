package com.zwstudio.logicpuzzlesandroid.common.android;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.LevelProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Game;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static android.app.AlertDialog.Builder;

@EActivity
public abstract class GameActivity<G extends Game<G, GM, GS>, GD extends GameDocument<G, GM>, GM, GS extends GameState>
        extends BaseActivity implements GameInterface<G, GM, GS> {
    public abstract GD doc();

    @ViewById
    protected View gameView;
    @ViewById
    protected TextView tvLevel;
    @ViewById
    protected TextView tvSolved;
    @ViewById
    protected TextView tvMoves;
    @ViewById
    protected TextView tvSolution;
    @ViewById
    protected Button btnSaveSolution;
    @ViewById
    protected Button btnLoadSolution;
    @ViewById
    protected Button btnDeleteSolution;

    public G game;
    protected boolean levelInitilizing;

    @AfterViews
    protected void init() {
        startGame();
    }

    @Click
    protected void btnUndo() {
        game.undo();
    }

    @Click
    protected void btnRedo() {
        game.redo();
    }

    @Click
    protected void btnClear() {
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

    abstract protected void startGame();

    @Override
    public void moveAdded(G game, GM move) {
        if (levelInitilizing) return;
        doc().moveAdded(game, move);
    }

    private void updateMovesUI(G game) {
        tvMoves.setText(String.format("Moves: %d(%d)", game.moveIndex(), game.moveCount()));
        tvSolved.setTextColor(game.isSolved() ? Color.WHITE : Color.BLACK);
        btnSaveSolution.setEnabled(game.isSolved());
    }

    @Override
    public void levelInitilized(G game, GS state) {
        gameView.invalidate();
        updateMovesUI(game);
    }

    @Override
    public void levelUpdated(G game, GS stateFrom, GS stateTo) {
        gameView.invalidate();
        updateMovesUI(game);
        if (!levelInitilizing) doc().levelUpdated(game);
    }

    @Override
    public void gameSolved(G game) {
        if (levelInitilizing) return;
        app.soundManager.playSoundSolved();
        doc().gameSolved(game);
        updateSolutionUI();
    }

    protected void updateSolutionUI() {
        LevelProgress rec = doc().levelProgressSolution();
        boolean hasSolution = rec.moveIndex != 0;
        tvSolution.setText("Solution: " + (!hasSolution ? "None" : String.valueOf(rec.moveIndex)));
        btnLoadSolution.setEnabled(hasSolution);
        btnDeleteSolution.setEnabled(hasSolution);
    }

    @Click
    protected void btnSaveSolution() {
        doc().saveSolution(game);
        updateSolutionUI();
    }

    @Click
    protected void btnLoadSolution() {
        doc().loadSolution();
        startGame();
    }

    @Click
    protected void btnDeleteSolution() {
        doc().deleteSolution();
        updateSolutionUI();
    }

}
