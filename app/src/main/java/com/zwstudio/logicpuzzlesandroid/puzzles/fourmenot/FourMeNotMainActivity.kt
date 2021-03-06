package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class FourMeNotMainActivity : GameMainActivity<FourMeNotGame, FourMeNotDocument, FourMeNotGameMove, FourMeNotGameState>() {
    @Bean
    protected lateinit var document: FourMeNotDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        FourMeNotOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        FourMeNotGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class FourMeNotOptionsActivity : GameOptionsActivity<FourMeNotGame, FourMeNotDocument, FourMeNotGameMove, FourMeNotGameState>() {
    @Bean
    protected lateinit var document: FourMeNotDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class FourMeNotHelpActivity : GameHelpActivity<FourMeNotGame, FourMeNotDocument, FourMeNotGameMove, FourMeNotGameState>() {
    @Bean
    protected lateinit var document: FourMeNotDocument
    override val doc get() = document
}