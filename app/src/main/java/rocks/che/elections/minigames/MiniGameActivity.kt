package rocks.che.elections.minigames

import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import im.delight.android.audio.MusicManager
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.imageResource
import rocks.che.elections.PollActivity
import rocks.che.elections.helpers.DefaultActivity

data class MovingImage(val imageFrame: FrameLayout, val imageView: ImageView, val speed: Float,
                       val onDestroy: (() -> Unit)? = null, var onAppear: ((Float) -> Unit)? = null)

const val frameRate = 60

abstract class MiniGameActivity(private val nextActivity: Class<out DefaultActivity> = PollActivity::class.java) : DefaultActivity() {
    protected val handler = Handler()
    protected var score = 0
    protected val maxLose = 5

    private val movingImages = mutableSetOf<MovingImage>()
    private lateinit var movingViewGroup: ViewGroup


    override fun onDestroy() {
        super.onDestroy()
        MusicManager.instance.resume()
    }

    open fun calculateMoney(): Int {
        return score
    }

    open fun lose() {
        handler.removeCallbacksAndMessages(null)
        val intent = Intent()
        intent.putExtra("money", calculateMoney());
        setResult(RESULT_OK, intent);
        finish();
    }

    protected fun createMovingImage(imgResource: Int, speed: Float = 1f, depth: Float = 1f,
                                    x: Float = displayMetrics.widthPixels.toFloat(),
                                    onDestroy: (() -> Unit)? = null, onAppear: ((Float) -> Unit)? = null) {
        val layout = FrameLayout(this)
        val imageView = ImageView(this)
        imageView.adjustViewBounds = true

        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        //params.leftMargin = 50;
        //params.topMargin = 60;

        imageView.imageResource = imgResource
        layout.x = x

        layout.addView(imageView, params)
        movingViewGroup.addView(layout)
        layout.translationZ = -depth
        movingImages.add(MovingImage(layout, imageView, speed, onDestroy, onAppear))
    }

    private fun updateMovingImages() {
        val toRemove: MutableList<MovingImage> = mutableListOf()

        for (mi in movingImages) {
            mi.imageFrame.x -= mi.speed

            if (mi.onAppear != null && mi.imageFrame.x < displayMetrics.widthPixels.toFloat()) {
                Log.d("shit", "imgf.width: %d".format(mi.imageView.width))
                mi.onAppear!!.invoke(mi.imageFrame.x + mi.imageView.width)
                mi.onAppear = null
            }

            if (mi.imageFrame.x <= -mi.imageFrame.width) {
                if (mi.onDestroy != null) {
                    mi.onDestroy.invoke()
                }
                toRemove.add(mi)
            }
        }

        for (mi in toRemove) {
            movingImages.remove(mi)
            movingViewGroup.removeView(mi.imageFrame)
        }

        handler.postDelayed({ updateMovingImages() }, (1000 / frameRate).toLong())
    }

    protected fun doEach(f: () -> Unit, t: () -> Double) {
        handler.postDelayed({ f(); doEach(f, t) }, (t() + 0.5).toLong())
    }

    protected fun doEach(f: () -> Unit, t: Double = 1000.0) = doEach(f, { t })

    protected fun useMovingImages(mvg: ViewGroup) {
        movingViewGroup = mvg
        updateMovingImages()
    }
}