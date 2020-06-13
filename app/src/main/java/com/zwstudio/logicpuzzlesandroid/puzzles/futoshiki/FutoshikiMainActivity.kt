package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class FutoshikiMainActivity : GameMainActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    @Bean
    protected lateinit var document: FutoshikiDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        FutoshikiOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        FutoshikiGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class FutoshikiOptionsActivity : GameOptionsActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    @Bean
    protected lateinit var document: FutoshikiDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class FutoshikiHelpActivity : GameHelpActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    @Bean
    protected lateinit var document: FutoshikiDocument
    override val doc get() = document
}