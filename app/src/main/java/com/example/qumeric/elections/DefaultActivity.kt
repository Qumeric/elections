package com.example.qumeric.elections

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.AnkoContext

abstract class DefaultActivity: AppCompatActivity() {
    override fun onBackPressed() {
        // Do nothing
    }

    fun drawInformationDialog(title: String, message: String, run: () -> Unit, ui: AnkoContext<DefaultActivity>) = with(ui) {
        val simpleAlert = AlertDialog.Builder(ctx).create()
        simpleAlert.setTitle(title)
        simpleAlert.setMessage(message)

        simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, ctx.getString(R.string.yes_button), {
            _, _ -> run()
        })

        simpleAlert.show()
    }
}