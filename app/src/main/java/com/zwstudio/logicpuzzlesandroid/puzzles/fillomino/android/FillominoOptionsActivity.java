package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.data.FillominoDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain.FillominoGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain.FillominoGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain.FillominoGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class FillominoOptionsActivity extends GameOptionsActivity<FillominoGame, FillominoDocument, FillominoGameMove, FillominoGameState> {
    @Bean
    protected FillominoDocument document;
    public FillominoDocument doc() {return document;}
}
