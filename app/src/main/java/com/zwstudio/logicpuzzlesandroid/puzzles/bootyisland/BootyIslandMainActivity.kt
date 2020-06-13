package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class BootyIslandMainActivity : GameMainActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState>() {
    @Bean
    protected lateinit var document: BootyIslandDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        BootyIslandOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        BootyIslandGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class BootyIslandOptionsActivity : GameOptionsActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState>() {
    @Bean
    protected lateinit var document: BootyIslandDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class BootyIslandHelpActivity : GameHelpActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState>() {
    @Bean
    protected lateinit var document: BootyIslandDocument
    override val doc get() = document
}
