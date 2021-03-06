package im.delight.android.audio

/*
 * Copyright (c) delight.im <info@delight.im>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context
import android.media.MediaPlayer

/** Plays music and one-off sound files while managing the resources efficiently  */
class MusicManager private constructor() {
    private var mMediaPlayer: MediaPlayer? = null
    private val oldPlayers = mutableListOf<MediaPlayer>()

    /**
     * Plays the sound with the given resource ID
     *
     * @param context a valid `Context` reference
     * @param soundResourceId the resource ID of the sound (e.g. `R.raw.my_sound`)
     */
    @Synchronized
    fun play(context: Context, soundResourceId: Int, stopLast: Boolean = false) {
        if (stopLast) {
            // if there's an existing stream playing already
            if (mMediaPlayer != null) {
                stop()
            }
        } else {
            if (mMediaPlayer != null) {
                pause()
                oldPlayers.add(mMediaPlayer!!)
            }
        }

        // create a new stream for the sound to play
        mMediaPlayer = MediaPlayer.create(context.applicationContext, soundResourceId)
        // set volume (same for both channels)
        mMediaPlayer!!.setVolume(volume, volume)
        // make it loop
        mMediaPlayer!!.isLooping = true

        // if the instance could be created
        if (mMediaPlayer != null) {
            // set a listener that is called when playback has been finished
            mMediaPlayer!!.setOnCompletionListener { mp ->
                // if the instance is set
                if (mp != null) {
                    mp.release()

                    mMediaPlayer = null
                }
            }

            mMediaPlayer!!.start()
        }
    }

    private fun stop() {
        try {
            mMediaPlayer?.stop()
        } catch (e: Exception) {
        }

        mMediaPlayer?.release()
        mMediaPlayer = null
    }

    fun pause() {
        try {
            mMediaPlayer?.pause()
        } catch (e: Exception) {
        }
    }

    fun play() {
        try {
            mMediaPlayer?.start()
        } catch (e: Exception) {
        }
    }

    fun resume() {
        if (oldPlayers.size > 0) {
            stop()
            mMediaPlayer = oldPlayers.last()
            oldPlayers.removeAt(oldPlayers.size - 1)
            play()
        }
    }

    companion object {
        private var mInstance: MusicManager? = null
        var volume: Float = 0.2f
            set(v) {
                field = maxOf(0f, minOf(v, 1f))
            }

        /**
         * Returns the single instance of this class
         *
         * @return the instance
         */
        val instance: MusicManager
            get() {
                if (mInstance == null) {
                    mInstance = MusicManager()
                }

                return mInstance!!
            }
    }
}
