package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class ParkLakesMainActivity : GameMainActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState>() {
    @Bean
    protected lateinit var document: ParkLakesDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        ParkLakesOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        ParkLakesGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class ParkLakesOptionsActivity : GameOptionsActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState>() {
    @Bean
    protected lateinit var document: ParkLakesDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class ParkLakesHelpActivity : GameHelpActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState>() {
    @Bean
    protected lateinit var document: ParkLakesDocument
    override val doc get() = document
}