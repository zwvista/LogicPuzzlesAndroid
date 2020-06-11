package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.data.FutoshikiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class FutoshikiMainActivity : GameMainActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    @Bean
    protected lateinit var document: FutoshikiDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        FutoshikiOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        FutoshikiGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class FutoshikiOptionsActivity : GameOptionsActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    @Bean
    protected lateinit var document: FutoshikiDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class FutoshikiHelpActivity : GameHelpActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    @Bean
    protected lateinit var document: FutoshikiDocument
    override fun doc() = document
}