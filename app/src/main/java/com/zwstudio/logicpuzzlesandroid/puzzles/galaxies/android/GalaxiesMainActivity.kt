package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.data.GalaxiesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class GalaxiesMainActivity : GameMainActivity<GalaxiesGame?, GalaxiesDocument?, GalaxiesGameMove?, GalaxiesGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: GalaxiesDocument? = null
    override fun doc() = document!!

    @Click
    fun btnOptions() {
        GalaxiesOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        GalaxiesGameActivity_.intent(this).start()
    }
}