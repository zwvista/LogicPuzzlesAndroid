package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.data.GardenerDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class GardenerOptionsActivity extends GameOptionsActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState> {
    @Bean
    protected GardenerDocument document;
    public GardenerDocument doc() {return document;}
}
