package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.data.LighthousesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class LighthousesOptionsActivity extends GameOptionsActivity<LighthousesGame, LighthousesDocument, LighthousesGameMove, LighthousesGameState> {
    @Bean
    protected LighthousesDocument document;
    public LighthousesDocument doc() {return document;}
}
