package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.data.TierraDelFuegoDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class TierraDelFuegoOptionsActivity extends GameOptionsActivity<TierraDelFuegoGame, TierraDelFuegoDocument, TierraDelFuegoGameMove, TierraDelFuegoGameState> {
    @Bean
    protected TierraDelFuegoDocument document;
    public TierraDelFuegoDocument doc() {return document;}
}
