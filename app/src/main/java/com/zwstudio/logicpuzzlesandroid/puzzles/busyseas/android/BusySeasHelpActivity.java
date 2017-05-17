package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.data.BusySeasDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class BusySeasHelpActivity extends GameHelpActivity<BusySeasGame, BusySeasDocument, BusySeasGameMove, BusySeasGameState> {
    @Bean
    protected BusySeasDocument document;
    public BusySeasDocument doc() {return document;}
}
