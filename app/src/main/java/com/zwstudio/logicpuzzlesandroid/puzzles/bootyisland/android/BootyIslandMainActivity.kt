package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.data.BootyIslandDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGame
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
open class BootyIslandMainActivity : GameMainActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState>() {
    @Bean
    protected lateinit var document: BootyIslandDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        BootyIslandOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        BootyIslandGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
open class BootyIslandOptionsActivity : GameOptionsActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState>() {
    @Bean
    protected lateinit var document: BootyIslandDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
open class BootyIslandHelpActivity : GameHelpActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState>() {
    @Bean
    protected lateinit var document: BootyIslandDocument
    override fun doc() = document
}
