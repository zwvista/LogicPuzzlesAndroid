package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.data.TapDifferentlyDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class TapDifferentlyGameActivity extends GameGameActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState> {
    @Bean
    protected TapDifferentlyDocument document;
    public TapDifferentlyDocument doc() {return document;}

    protected TapDifferentlyGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new TapDifferentlyGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(selectedLevelID)).orSome(0)).layout;
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new TapDifferentlyGame(layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                TapDifferentlyGameMove move = doc().loadMove(rec);
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
        TapDifferentlyHelpActivity_.intent(this).start();
    }
}
