package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.data.BattleShipsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class BattleShipsGameActivity extends GameGameActivity<BattleShipsGame, BattleShipsDocument, BattleShipsGameMove, BattleShipsGameState> {
    @Bean
    protected BattleShipsDocument document;
    public BattleShipsDocument doc() {return document;}

    protected BattleShipsGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new BattleShipsGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(selectedLevelID)).orSome(0)).layout;
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new BattleShipsGame(layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                BattleShipsGameMove move = doc().loadMove(rec);
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
        BattleShipsHelpActivity_.intent(this).start();
    }
}
