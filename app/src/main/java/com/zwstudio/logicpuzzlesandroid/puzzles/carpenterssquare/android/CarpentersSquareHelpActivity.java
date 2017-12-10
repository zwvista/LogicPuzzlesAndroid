package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.data.CarpentersSquareDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class CarpentersSquareHelpActivity extends GameHelpActivity<CarpentersSquareGame, CarpentersSquareDocument, CarpentersSquareGameMove, CarpentersSquareGameState> {
    @Bean
    protected CarpentersSquareDocument document;
    public CarpentersSquareDocument doc() {return document;}
}
