package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGame
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
open class LineSweeperOptionsActivity : GameOptionsActivity<LineSweeperGame?, LineSweeperDocument?, LineSweeperGameMove?, LineSweeperGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LineSweeperDocument? = null
    override fun doc(): LineSweeperDocument {
        return document!!
    }

    protected fun onDefault() {}
}