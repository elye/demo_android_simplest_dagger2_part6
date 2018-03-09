package com.elyeproj.simplestappwithdagger2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.Component
import dagger.Subcomponent
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Scope
import javax.inject.Singleton

class MainActivity : AppCompatActivity() {

    private lateinit var mainBox: SingletonBox
    private lateinit var magicBox: MagicBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainBox = DaggerSingletonBox.create()
        magicBox = mainBox.getMagicBox()

        btn_create.setOnClickListener {
            magicBox = mainBox.getMagicBox()
            useStorage()
        }
        btn_reuse.setOnClickListener {
            useStorage()
        }
    }

    private fun useStorage() {
        val storage = Storage()
        magicBox.poke(storage)
        text_view.text = "\nSingletonOne ${storage.singletonOne.count} " +
                "\nUniqueMagic ${storage.uniqueMagic.count}" +
                "\nNormalMagic ${storage.normalMagic.count}"
    }
}

@Singleton
@Component
interface SingletonBox {
    fun getMagicBox(): MagicBox
}

@MagicScope
@Subcomponent
interface MagicBox {
    fun poke(storage: Storage)
}

class Storage {
    @Inject lateinit var singletonOne: SingletonOne
    @Inject lateinit var uniqueMagic: UniqueMagic
    @Inject lateinit var normalMagic: NormalMagic
}

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class MagicScope

var staticCounter = 0

@Singleton
class SingletonOne @Inject constructor() {
    val count = staticCounter++
}

@MagicScope
class UniqueMagic @Inject constructor() {
    val count = staticCounter++
}

class NormalMagic @Inject constructor() {
    val count = staticCounter++
}
