package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.data.GardenerDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class GardenerHelpActivity extends GameHelpActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState> {
    @Bean
    protected GardenerDocument document;
    public GardenerDocument doc() {return document;}
}
