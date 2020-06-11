package com.zwstudio.logicpuzzlesandroid.puzzles.parks.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.data.ParksDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGame
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class ParksMainActivity : GameMainActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState>() {
    @Bean
    protected lateinit var document: ParksDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        ParksOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        ParksGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class ParksOptionsActivity : GameOptionsActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState>() {
    @Bean
    protected lateinit var document: ParksDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class ParksHelpActivity : GameHelpActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState>() {
    @Bean
    protected lateinit var document: ParksDocument
    override fun doc() = document
}