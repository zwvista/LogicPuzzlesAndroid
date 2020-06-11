package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGame
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameState
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.android.NurikabeGameActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.android.NurikabeOptionsActivity_
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class NurikabeMainActivity : GameMainActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    @Bean
    protected lateinit var document: NurikabeDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        NurikabeOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        NurikabeGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class NurikabeOptionsActivity : GameOptionsActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    @Bean
    protected lateinit var document: NurikabeDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class NurikabeHelpActivity : GameHelpActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    @Bean
    protected lateinit var document: NurikabeDocument
    override fun doc() = document
}