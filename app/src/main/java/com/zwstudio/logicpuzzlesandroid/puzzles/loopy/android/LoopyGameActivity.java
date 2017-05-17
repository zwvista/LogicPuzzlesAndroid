package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.data.LoopyDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class LoopyGameActivity extends GameGameActivity<LoopyGame, LoopyDocument, LoopyGameMove, LoopyGameState> {
    @Bean
    protected LoopyDocument document;
    public LoopyDocument doc() {return document;}

    protected LoopyGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new LoopyGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o._1().equals(selectedLevelID)).orSome(0))._2();
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new LoopyGame(layout, this);
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                LoopyGameMove move = doc().loadMove(rec);
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
        LoopyHelpActivity_.intent(this).start();
    }
}
