package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.data.MinesweeperDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGame
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class MinesweeperMainActivity : GameMainActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState>() {
    @Bean
    protected lateinit var document: MinesweeperDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        MinesweeperOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        MinesweeperGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class MinesweeperOptionsActivity : GameOptionsActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState>() {
    @Bean
    protected lateinit var document: MinesweeperDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class MinesweeperHelpActivity : GameHelpActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState>() {
    @Bean
    protected lateinit var document: MinesweeperDocument
    override fun doc() = document
}