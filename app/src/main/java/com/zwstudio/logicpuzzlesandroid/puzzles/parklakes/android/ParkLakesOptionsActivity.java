package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.data.ParkLakesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class ParkLakesOptionsActivity extends GameOptionsActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState> {
    @Bean
    protected ParkLakesDocument document;
    public ParkLakesDocument doc() {return document;}
}
