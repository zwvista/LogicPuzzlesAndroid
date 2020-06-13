package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGame
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class LineSweeperMainActivity : GameMainActivity<LineSweeperGame, LineSweeperDocument, LineSweeperGameMove, LineSweeperGameState>() {
    @Bean
    protected lateinit var document: LineSweeperDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        LineSweeperOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        LineSweeperGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class LineSweeperOptionsActivity : GameOptionsActivity<LineSweeperGame, LineSweeperDocument, LineSweeperGameMove, LineSweeperGameState>() {
    @Bean
    protected lateinit var document: LineSweeperDocument
    override val doc get() = document

    protected fun onDefault() {}
}

@EActivity(R.layout.activity_game_help)
class LineSweeperHelpActivity : GameHelpActivity<LineSweeperGame, LineSweeperDocument, LineSweeperGameMove, LineSweeperGameState>() {
    @Bean
    protected lateinit var document: LineSweeperDocument
    override val doc get() = document
}