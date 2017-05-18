package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.data.MineShipsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class MineShipsOptionsActivity extends GameOptionsActivity<MineShipsGame, MineShipsDocument, MineShipsGameMove, MineShipsGameState> {
    @Bean
    protected MineShipsDocument document;
    public MineShipsDocument doc() {return document;}
}
