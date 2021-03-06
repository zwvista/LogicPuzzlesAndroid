package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class LighthousesMainActivity : GameMainActivity<LighthousesGame, LighthousesDocument, LighthousesGameMove, LighthousesGameState>() {
    @Bean
    protected lateinit var document: LighthousesDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        LighthousesOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        LighthousesGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class LighthousesOptionsActivity : GameOptionsActivity<LighthousesGame, LighthousesDocument, LighthousesGameMove, LighthousesGameState>() {
    @Bean
    protected lateinit var document: LighthousesDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class LighthousesHelpActivity : GameHelpActivity<LighthousesGame, LighthousesDocument, LighthousesGameMove, LighthousesGameState>() {
    @Bean
    protected lateinit var document: LighthousesDocument
    override val doc get() = document
}