package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.data.BalancedTapasDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class BalancedTapasHelpActivity extends GameHelpActivity<BalancedTapasGame, BalancedTapasDocument, BalancedTapasGameMove, BalancedTapasGameState> {
    @Bean
    protected BalancedTapasDocument document;
    public BalancedTapasDocument doc() {return document;}
}
