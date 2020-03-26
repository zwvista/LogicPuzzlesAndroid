package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
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
    protected var document: BootyIslandDocument? = null

    override fun doc(): BootyIslandDocument? {
        return document
    }

    @Click
    internal fun btnOptions() {
        BootyIslandOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc()!!.resumeGame()
        BootyIslandGameActivity_.intent(this).start()
    }
}
