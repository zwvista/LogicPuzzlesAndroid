package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.android.OrchardsGameActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.android.OrchardsOptionsActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.data.OrchardsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class OrchardsMainActivity : GameMainActivity<OrchardsGame?, OrchardsDocument?, OrchardsGameMove?, OrchardsGameState?>() {
    @Bean
    protected var document: OrchardsDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        OrchardsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        OrchardsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class OrchardsOptionsActivity : GameOptionsActivity<OrchardsGame?, OrchardsDocument?, OrchardsGameMove?, OrchardsGameState?>() {
    @Bean
    protected var document: OrchardsDocument? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class OrchardsHelpActivity : GameHelpActivity<OrchardsGame?, OrchardsDocument?, OrchardsGameMove?, OrchardsGameState?>() {
    @Bean
    protected var document: OrchardsDocument? = null
    override fun doc() = document
}