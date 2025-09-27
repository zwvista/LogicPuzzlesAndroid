package com.zwstudio.logicpuzzlesandroid.home.android

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.AbcDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.ABCPathDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.AbstractPaintingDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.BalancedTapasDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.BattleShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.BootyIslandDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.BoxItAgainDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.BoxItAroundDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.BoxItUpDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.branches.BranchesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.BridgesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.BusySeasDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.BWTapaDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.CalcudokuDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.CarpentersSquareDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.CarpentersWallDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.CastleBaileyDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.CloudsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.DigitalBattleShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.DisconnectFourDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.DominoDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.FenceItUpDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.FenceLitsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.FenceSentinelsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.FillominoDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.FourMeNotDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.FutoshikiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.GalaxiesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.GardenerDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.HitoriDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.HolidayIslandDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.KakurasuDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.KakuroDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.KropkiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.LightBattleShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.LightenUpDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.LighthousesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.LineSweeperDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.LitsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.LoopyDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.MagnetsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.makethedifference.MaketheDifferenceDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.MasyuDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.MathraxDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.MineShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.MinesweeperDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.MiniLitsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.MosaikDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.NeighboursDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing.NorthPoleFishingDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.NoughtsAndCrossesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.NumberLinkDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.NumberPathDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.NurikabeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.OrchardsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.OverUnderDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.PaintTheNurikabeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.PairakabeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.ParkLakesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.ParksDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.PataDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.PowerGridDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.ProductSentinelsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.RippleEffectDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.RobotCrosswordsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.RobotFencesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.RoomsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.SentinelsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.SkyscrapersDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.SlitherLinkDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.SnailDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.SnakeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.SnakeObject
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.Square100Document
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.SumscrapersDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.TapaDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.TapaIslandsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.TapAlikeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.TapARowDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.TapDifferentlyDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.TatamiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.TataminoDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.TennerGridDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.TentsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.TheOddBrickDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.TierraDelFuegoDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.WallsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.WallSentinelsDocument
import io.realm.Realm
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

// https://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/48767617#48767617
class LogicPuzzlesApplication : Application(), LifecycleObserver {
    val homeDocument: HomeDocument by inject()
    private val soundManager: SoundManager by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Koin Android logger
            androidLogger(Level.ERROR)
            //inject Android context
            androidContext(this@LogicPuzzlesApplication)
            // use modules
            modules(logicPuzzlesModule)
        }
        Realm.init(this)
        realm = Realm.getDefaultInstance()
        soundManager.init()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
    private val logicPuzzlesModule = module {
        single { SoundManager(androidContext() as LogicPuzzlesApplication) }
        single { HomeDocument() }
        single { AbcDocument(androidContext()) }
        single { ABCPathDocument(androidContext()) }
        single { AbstractPaintingDocument(androidContext()) }
        single { BWTapaDocument(androidContext()) }
        single { BalancedTapasDocument(androidContext()) }
        single { BattleShipsDocument(androidContext()) }
        single { BootyIslandDocument(androidContext()) }
        single { BoxItAgainDocument(androidContext()) }
        single { BoxItAroundDocument(androidContext()) }
        single { BoxItUpDocument(androidContext()) }
        single { BranchesDocument(androidContext()) }
        single { BridgesDocument(androidContext()) }
        single { BusySeasDocument(androidContext()) }
        single { CalcudokuDocument(androidContext()) }
        single { CarpentersSquareDocument(androidContext()) }
        single { CarpentersWallDocument(androidContext()) }
        single { CastleBaileyDocument(androidContext()) }
        single { CloudsDocument(androidContext()) }
        single { DigitalBattleShipsDocument(androidContext()) }
        single { DisconnectFourDocument(androidContext()) }
        single { DominoDocument(androidContext()) }
        single { FenceItUpDocument(androidContext()) }
        single { FenceLitsDocument(androidContext()) }
        single { FenceSentinelsDocument(androidContext()) }
        single { FillominoDocument(androidContext()) }
        single { FourMeNotDocument(androidContext()) }
        single { FutoshikiDocument(androidContext()) }
        single { GalaxiesDocument(androidContext()) }
        single { GardenerDocument(androidContext()) }
        single { HitoriDocument(androidContext()) }
        single { HolidayIslandDocument(androidContext()) }
        single { KakurasuDocument(androidContext()) }
        single { KakuroDocument(androidContext()) }
        single { KropkiDocument(androidContext()) }
        single { LightBattleShipsDocument(androidContext()) }
        single { LightenUpDocument(androidContext()) }
        single { LighthousesDocument(androidContext()) }
        single { LineSweeperDocument(androidContext()) }
        single { LitsDocument(androidContext()) }
        single { LoopyDocument(androidContext()) }
        single { MagnetsDocument(androidContext()) }
        single { MaketheDifferenceDocument(androidContext()) }
        single { MasyuDocument(androidContext()) }
        single { MathraxDocument(androidContext()) }
        single { MineShipsDocument(androidContext()) }
        single { MinesweeperDocument(androidContext()) }
        single { MiniLitsDocument(androidContext()) }
        single { MosaikDocument(androidContext()) }
        single { NeighboursDocument(androidContext()) }
        single { NorthPoleFishingDocument(androidContext()) }
        single { NoughtsAndCrossesDocument(androidContext()) }
        single { NumberLinkDocument(androidContext()) }
        single { NumberPathDocument(androidContext()) }
        single { NurikabeDocument(androidContext()) }
        single { OrchardsDocument(androidContext()) }
        single { OverUnderDocument(androidContext()) }
        single { PaintTheNurikabeDocument(androidContext()) }
        single { PairakabeDocument(androidContext()) }
        single { ParkLakesDocument(androidContext()) }
        single { ParksDocument(androidContext()) }
        single { PataDocument(androidContext()) }
        single { PowerGridDocument(androidContext()) }
        single { ProductSentinelsDocument(androidContext()) }
        single { RippleEffectDocument(androidContext()) }
        single { RobotCrosswordsDocument(androidContext()) }
        single { RobotFencesDocument(androidContext()) }
        single { RoomsDocument(androidContext()) }
        single { SentinelsDocument(androidContext()) }
        single { SkyscrapersDocument(androidContext()) }
        single { SlitherLinkDocument(androidContext()) }
        single { SnailDocument(androidContext()) }
        single { SnakeDocument(androidContext()) }
        single { Square100Document(androidContext()) }
        single { SumscrapersDocument(androidContext()) }
        single { TapARowDocument(androidContext()) }
        single { TapAlikeDocument(androidContext()) }
        single { TapDifferentlyDocument(androidContext()) }
        single { TapaDocument(androidContext()) }
        single { TapaIslandsDocument(androidContext()) }
        single { TatamiDocument(androidContext()) }
        single { TataminoDocument(androidContext()) }
        single { TennerGridDocument(androidContext()) }
        single { TentsDocument(androidContext()) }
        single { TheOddBrickDocument(androidContext()) }
        single { TierraDelFuegoDocument(androidContext()) }
        single { WallSentinelsDocument(androidContext()) }
        single { WallsDocument(androidContext()) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        soundManager.activityStarted()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        soundManager.activityStopped()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        soundManager.doUnbindService()
    }
}

lateinit var realm: Realm
