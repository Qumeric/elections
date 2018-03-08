package rocks.che.elections.helpers
/*
import org.jetbrains.anko.AnkoContext
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.ViewManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import rocks.che.elections.R

class RichView : LinearLayout {
    private lateinit var image: ImageView
    private lateinit var text: TextView

    private fun init() = AnkoContext.createDelegate(this).apply {
        verticalLayout {
            gravity = CENTER

            space { }.lparams(weight = 0.1f, height = 0)

            gameTextView(20) {
                textResource = R.string.your_candidate
            }.lparams(weight = 0.1f, height = 0)

            imageView {
                backgroundResource = R.color.blue
            }.lparams(weight = 0.01f, height = 0, width = dip(120))

            gameTextView(16) {
                text = candidate.name
            }.lparams(weight = 0.09f, height = 0)
            padding = dip(24)
        }
    }

    constructor(context: Context?, h1:String, h2:String?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.myRichView(h1:String, h2:String? = null, theme: Int = 0) = myRichView(h1, h2, {}, theme)
inline fun ViewManager.myRichView(h1:String, h2:String? = null, init: RichView.() -> Unit,
                                  theme: Int = 0) = ankoView({RichView(it, h1, h2)}, theme, init)
                                  */
