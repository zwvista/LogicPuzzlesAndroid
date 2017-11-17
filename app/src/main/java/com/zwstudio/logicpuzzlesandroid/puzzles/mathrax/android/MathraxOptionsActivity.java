package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.data.MathraxDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class MathraxOptionsActivity extends GameOptionsActivity<MathraxGame, MathraxDocument, MathraxGameMove, MathraxGameState> {
    @Bean
    protected MathraxDocument document;
    public MathraxDocument doc() {return document;}
}
