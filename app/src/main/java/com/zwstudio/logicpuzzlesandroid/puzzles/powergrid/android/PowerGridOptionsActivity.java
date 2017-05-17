package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.data.PowerGridDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class PowerGridOptionsActivity extends GameOptionsActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState> {
    @Bean
    protected PowerGridDocument document;
    public PowerGridDocument doc() {return document;}
}
