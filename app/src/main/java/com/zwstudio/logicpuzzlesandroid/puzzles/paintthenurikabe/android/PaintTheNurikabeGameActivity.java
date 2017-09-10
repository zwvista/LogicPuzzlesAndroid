package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.data.PaintTheNurikabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class PaintTheNurikabeGameActivity extends GameGameActivity<PaintTheNurikabeGame, PaintTheNurikabeDocument, PaintTheNurikabeGameMove, PaintTheNurikabeGameState> {
    @Bean
    protected PaintTheNurikabeDocument document;
    public PaintTheNurikabeDocument doc() {return document;}

    protected PaintTheNurikabeGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new PaintTheNurikabeGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        GameLevel level = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(selectedLevelID)).orSome(0));
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new PaintTheNurikabeGame(level.layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                PaintTheNurikabeGameMove move = doc().loadMove(rec);
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

    @Click
    protected void btnHelp() {
        PaintTheNurikabeHelpActivity_.intent(this).start();
    }
}
