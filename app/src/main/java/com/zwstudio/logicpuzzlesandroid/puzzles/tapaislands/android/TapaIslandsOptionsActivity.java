package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.data.TapaIslandsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class TapaIslandsOptionsActivity extends GameOptionsActivity<TapaIslandsGame, TapaIslandsDocument, TapaIslandsGameMove, TapaIslandsGameState> {
    @Bean
    protected TapaIslandsDocument document;
    public TapaIslandsDocument doc() {return document;}
}
