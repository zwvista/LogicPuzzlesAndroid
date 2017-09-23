package com.zwstudio.logicpuzzlesandroid.puzzles.snake.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.data.SnakeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class SnakeOptionsActivity extends GameOptionsActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState> {
    @Bean
    protected SnakeDocument document;
    public SnakeDocument doc() {return document;}
}
