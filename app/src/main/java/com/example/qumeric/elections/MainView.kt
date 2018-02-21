package com.example.qumeric.elections

import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick

class MainView : AnkoComponent<MainActivity> {
    private lateinit var ankoContext: AnkoContext<MainActivity>

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER
            id = R.id.formLogin

            for (candidate in loadCandidates(resources)) {
                val b = button {
                    onClick {
                        gamestate = Gamestate(candidate, loadQuestions(resources),
                                loadFakeCandidates(resources))
                        ctx.startActivity(ctx.intentFor<GameActivity>())
                    }
                }.lparams(width= matchParent, height = wrapContent)
                b.setText(candidate.name)
            }
            button {
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
            }
        }
    }

    /*private fun handleOnSignInButtonPressed(username: String, password: String) {
        with(ankoContext) {
            if (username.isBlank() or password.isBlank()) {
                alert(title = R.string.sign_in_alert_invalid_user_title, message = R.string.sign_in_alert_invalid_user_message) {
                    positiveButton(R.string.dialog_button_close) {}
                }.show()
            } else {
                owner.authorizeUser(username, password)
            }
        }
    }

    fun showAccessDeniedAlertDialog() {
        with(ankoContext) {
            alert(title = R.string.sign_in_alert_access_denied_title, message = R.string.sign_in_alert_access_denied_msg) {
                positiveButton(R.string.dialog_button_close) {}
            }.show()
        }
    }*/
}