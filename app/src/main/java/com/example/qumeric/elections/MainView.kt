package com.example.qumeric.elections

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.CardView
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.widget.GridLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.sdk25.listeners.onClick

inline fun ViewManager.cardView(init: CardView.() -> Unit): CardView {
    return ankoView({ CardView(it) }, theme = 0, init = init)
}

class MainView : AnkoComponent<MainActivity> {
    private lateinit var ankoContext: AnkoContext<MainActivity>

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        ankoContext = ui

        var row = 0
        var col = 0

        verticalLayout {
            weightSum = 1f
            gravity = Gravity.CENTER
            verticalLayout {
                gravity = Gravity.CENTER
                textView {
                    textResource = R.string.choose_candidate
                    typeface = ResourcesCompat.getFont(ctx, R.font.mfred)
                    textSize = dip(20).toFloat()
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                }
            }.lparams {
                height = matchParent
                weight = 0.9f
            }

            gridLayout {
                rowCount = 3
                columnCount = 2
                alignmentMode = GridLayout.ALIGN_MARGINS
                padding = dip(14)

                for (candidate in loadCandidates(resources)) {
                    cardView {
                        cardElevation = dip(8).toFloat()
                        radius = 0f

                        verticalLayout {
                            gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL

                            linearLayout {
                                gravity = Gravity.CENTER

                                imageView {
                                    imageResource = candidate.resource
                                }.lparams {
                                    width = dip(80)
                                }

                                verticalLayout {
                                    for ((group, value) in candidate.opinions) {
                                        textView {
                                            text = "%s: %d".format(group, value.value)
                                            typeface = ResourcesCompat.getFont(ctx, R.font.mfred)
                                            textSize = dip(8).toFloat()
                                        }
                                    }
                                }
                            }.lparams {
                                height = 0
                                weight = 0.8f
                            }

                            button {
                                text = candidate.name
                                backgroundResource = R.color.brightBlue
                                onClick {
                                    gamestate = Gamestate(candidate, loadQuestions(resources),
                                            loadFakeCandidates(resources))
                                    ctx.startActivity(ctx.intentFor<GameActivity>())
                                }
                            }.lparams {
                                width = matchParent
                                height = 0
                                weight = 0.2f
                                margin = 0
                            }
                        }
                    }.lparams {
                        width = 0
                        height = 0
                        leftMargin = dip(8)
                        rightMargin = dip(8)
                        bottomMargin = dip(16)
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                    }
                    col++
                    if (col == 2) {
                        col = 0
                        row++
                    }
                }
                /*button {
                    text = "catcher"
                    onClick {
                        ctx.startActivity(ctx.intentFor<CatcherGameActivity>())
                    }
                }
                button {
                    text = "ducks"
                    onClick {
                        ctx.startActivity(ctx.intentFor<DucksGameActivity>())
                    }
                }
                button {
                    text = "hammer"
                    onClick {
                        ctx.startActivity(ctx.intentFor<HammerGameActivity>())
                    }
                }
                button {
                    text = "ladder"
                    onClick {
                        ctx.startActivity(ctx.intentFor<LadderGameActivity>())
                    }
                }
                button {
                    text = "snake"
                    onClick {
                        ctx.startActivity(ctx.intentFor<SnakeGameActivity>())
                    }
                }*/
            }.lparams {
                width = matchParent
                height = matchParent
                weight = 0.1f
            }
        }
    }
}
