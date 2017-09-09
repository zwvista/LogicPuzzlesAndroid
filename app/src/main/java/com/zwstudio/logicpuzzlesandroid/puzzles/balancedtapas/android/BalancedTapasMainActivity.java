package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.data.BalancedTapasDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class BalancedTapasMainActivity extends GameMainActivity<BalancedTapasGame, BalancedTapasDocument, BalancedTapasGameMove, BalancedTapasGameState> {
    @Bean
    protected BalancedTapasDocument document;
    public BalancedTapasDocument doc() {return document;}

    @Click
    void btnOptions() {
        BalancedTapasOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        BalancedTapasGameActivity_.intent(this).start();
    }
}
