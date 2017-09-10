package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.data.MinesweeperDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class MinesweeperGameActivity extends GameGameActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState> {
    @Bean
    protected MinesweeperDocument document;
    public MinesweeperDocument doc() {return document;}

    protected MinesweeperGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new MinesweeperGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(selectedLevelID)).orSome(0)).layout;
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new MinesweeperGame(layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                MinesweeperGameMove move = doc().loadMove(rec);
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
        MinesweeperHelpActivity_.intent(this).start();
    }
}
