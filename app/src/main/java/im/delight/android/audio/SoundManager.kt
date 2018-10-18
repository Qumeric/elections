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

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

/** Plays sounds (frequently and fast) while managing the resources efficiently  */
@SuppressLint("UseSparseArrays")
class SoundManager(context: Context, maxSimultaneousStreams: Int) : Thread() {
    private val mSoundPool: SoundPool? = SoundPool.Builder()
        .setMaxStreams(maxSimultaneousStreams)
        .setAudioAttributes(AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
        .build()
    private val mContext: Context = context.applicationContext
    private val mSounds: MutableMap<Int, Int>? = HashMap()
    private val mTasks = LinkedBlockingQueue<SoundManagerTask>()
    @Volatile
    private var mCancelled: Boolean = false

    fun load(soundResourceId: Int) {
        try {
            this.mTasks.put(SoundManagerTask.load(soundResourceId))
        } catch (e: InterruptedException) {
        }

    }

    fun play(soundResourceId: Int) {
        this.play(soundResourceId, 1.0f)
    }

    fun play(soundResourceId: Int, volume: Float) {
        this.play(soundResourceId, volume, 0)
    }

    fun play(soundResourceId: Int, volume: Float, repetitions: Int) {
        if (!this.isAlive) {
            return
        }

        try {
            this.mTasks.put(SoundManagerTask.play(soundResourceId, volume, repetitions))
        } catch (e: InterruptedException) {
        }

    }

    fun unload(soundResourceId: Int) {
        try {
            this.mTasks.put(SoundManagerTask.unload(soundResourceId))
        } catch (e: InterruptedException) {
        }

    }

    fun cancel() {
        try {
            this.mTasks.put(SoundManagerTask.cancel())
        } catch (e: InterruptedException) {
        }

    }

    override fun run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)

        try {
            while (!this.mCancelled) {
                val task = mTasks.take()
                if (task.isCancel) {
                    mCancelled = true
                    break
                } else {
                    var currentMapping: Int? = null
                    synchronized(mSounds!!) {
                        currentMapping = mSounds[task.soundResourceId]
                    }

                    if (task.isLoad) {
                        if (currentMapping == null) {
                            val newMapping = mSoundPool!!.load(mContext, task.soundResourceId, 1)

                            synchronized(mSounds) {
                                mSounds.put(task.soundResourceId, newMapping)
                            }
                        }
                    } else if (task.isPlay) {
                        if (currentMapping != null) {
                            mSoundPool!!.play(currentMapping!!, task.volume, task.volume, 0, task.repetitions, 1.0f)
                        }
                    } else if (task.isUnload) {
                        if (currentMapping != null) {
                            mSoundPool!!.unload(currentMapping!!)

                            synchronized(mSounds) {
                                mSounds.remove(task.soundResourceId)
                            }
                        }
                    }
                }
            }
        } catch (e: InterruptedException) {
        }

        if (mSounds != null) {
            synchronized(mSounds) {
                mSounds.clear()
            }
        }

        mSoundPool?.release()
    }

    private class SoundManagerTask private constructor(val soundResourceId: Int, val volume: Float, val repetitions: Int, private val mAction: Int) {
        val isLoad: Boolean
            get() = this.mAction == SoundManager.SoundManagerTask.ACTION_LOAD

        val isPlay: Boolean
            get() = this.mAction == SoundManager.SoundManagerTask.ACTION_PLAY

        val isUnload: Boolean
            get() = this.mAction == SoundManager.SoundManagerTask.ACTION_UNLOAD

        val isCancel: Boolean
            get() = this.mAction == SoundManager.SoundManagerTask.ACTION_CANCEL

        companion object {
            private const val ACTION_LOAD = 1
            private const val ACTION_PLAY = 2
            private const val ACTION_UNLOAD = 3
            private const val ACTION_CANCEL = 4

            fun load(soundResourceId: Int): SoundManager.SoundManagerTask {
                return SoundManager.SoundManagerTask(soundResourceId, 0f, 0, ACTION_LOAD)
            }

            fun play(soundResourceId: Int, volume: Float, repetitions: Int): SoundManager.SoundManagerTask {
                return SoundManager.SoundManagerTask(soundResourceId, volume, repetitions, ACTION_PLAY)
            }

            fun unload(soundResourceId: Int): SoundManager.SoundManagerTask {
                return SoundManager.SoundManagerTask(soundResourceId, 0f, 0, ACTION_UNLOAD)
            }

            fun cancel(): SoundManager.SoundManagerTask {
                return SoundManager.SoundManagerTask(0, 0f, 0, SoundManager.SoundManagerTask.ACTION_CANCEL)
            }
        }
    }
}
