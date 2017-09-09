package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.data.TapaDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class TapaOptionsActivity extends GameOptionsActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState> {
    @Bean
    protected TapaDocument document;
    public TapaDocument doc() {return document;}
}
