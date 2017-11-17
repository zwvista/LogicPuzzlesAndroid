package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.data.TierraDelFuegoDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class TierraDelFuegoHelpActivity extends GameHelpActivity<TierraDelFuegoGame, TierraDelFuegoDocument, TierraDelFuegoGameMove, TierraDelFuegoGameState> {
    @Bean
    protected TierraDelFuegoDocument document;
    public TierraDelFuegoDocument doc() {return document;}
}
