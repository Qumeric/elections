package im.delight.android.audio;

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

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// All code was converted to Kotlin using Android Studio built-in tool

/** Plays sounds (frequently and fast) while managing the resources efficiently */
@SuppressLint("UseSparseArrays")
public class SoundManager extends Thread {

	private final SoundPool mSoundPool;
	private final Context mContext;
	private final Map<Integer, Integer> mSounds;
	private final BlockingQueue<SoundManagerTask> mTasks = new LinkedBlockingQueue<SoundManagerTask>();
	private volatile boolean mCancelled;
	@SuppressWarnings("deprecation")
	public SoundManager(Context context, int maxSimultaneousStreams) {
        this.mSoundPool = new SoundPool(maxSimultaneousStreams, AudioManager.STREAM_MUSIC, 0);
        this.mContext = context.getApplicationContext();
        this.mSounds = new HashMap<Integer, Integer>();
	}

	public void load(int soundResourceId) {
		try {
            this.mTasks.put(SoundManagerTask.load(soundResourceId));
		}
		catch (InterruptedException e) { }
	}

	public void play(int soundResourceId) {
        this.play(soundResourceId, 1.0f);
	}

	public void play(int soundResourceId, float volume) {
        this.play(soundResourceId, volume, 0);
	}

	public void play(int soundResourceId, float volume, int repetitions) {
		if (!this.isAlive()) {
			return;
		}

		try {
            this.mTasks.put(SoundManagerTask.play(soundResourceId, volume, repetitions));
		}
		catch (InterruptedException e) { }
	}

	public void unload(int soundResourceId) {
		try {
            this.mTasks.put(SoundManagerTask.unload(soundResourceId));
		}
		catch (InterruptedException e) { }
	}

	public void cancel() {
		try {
            this.mTasks.put(SoundManagerTask.cancel());
		}
		catch (InterruptedException e) { }
	}

	@Override
	public void run() {
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

		try {
            while (!this.mCancelled) {
                SoundManager.SoundManagerTask task = mTasks.take();
                if (task.isCancel()) {
					mCancelled = true;
					break;
				}
				else {
					final Integer currentMapping;
					synchronized (mSounds) {
						currentMapping = mSounds.get(task.getSoundResourceId());
					}

					if (task.isLoad()) {
						if (currentMapping == null) {
							final int newMapping = mSoundPool.load(mContext, task.getSoundResourceId(), 1);

							synchronized (mSounds) {
								mSounds.put(task.getSoundResourceId(), newMapping);
							}
						}
					}
					else if (task.isPlay()) {
						if (currentMapping != null) {
							mSoundPool.play(currentMapping.intValue(), task.getVolume(), task.getVolume(), 0, task.getRepetitions(), 1.0f);
						}
					}
					else if (task.isUnload()) {
						if (currentMapping != null) {
							mSoundPool.unload(currentMapping.intValue());

							synchronized (mSounds) {
								mSounds.remove(task.getSoundResourceId());
							}
						}
					}
				}
			}
		}
		catch (InterruptedException e) { }

		if (mSounds != null) {
			synchronized (mSounds) {
				mSounds.clear();
			}
		}

		if (mSoundPool != null) {
			mSoundPool.release();
		}
	}

	private static class SoundManagerTask {

		private static final int ACTION_LOAD = 1;
		private static final int ACTION_PLAY = 2;
		private static final int ACTION_UNLOAD = 3;
		private static final int ACTION_CANCEL = 4;
		private final int mSoundResourceId;
		private final float mVolume;
		private final int mRepetitions;
		private final int mAction;

		private SoundManagerTask(final int soundResourceId, final float volume, final int repetitions, final int action) {
			mSoundResourceId = soundResourceId;
			mVolume = volume;
			mRepetitions = repetitions;
			mAction = action;
		}

		public static SoundManager.SoundManagerTask load(final int soundResourceId) {
			return new SoundManager.SoundManagerTask(soundResourceId, 0, 0, ACTION_LOAD);
		}

		public static SoundManager.SoundManagerTask play(final int soundResourceId, final float volume, final int repetitions) {
			return new SoundManager.SoundManagerTask(soundResourceId, volume, repetitions, ACTION_PLAY);
		}

		public static SoundManager.SoundManagerTask unload(final int soundResourceId) {
			return new SoundManager.SoundManagerTask(soundResourceId, 0, 0, ACTION_UNLOAD);
		}

		public static SoundManager.SoundManagerTask cancel() {
			return new SoundManager.SoundManagerTask(0, 0, 0, SoundManager.SoundManagerTask.ACTION_CANCEL);
		}

		public int getSoundResourceId() {
			return this.mSoundResourceId;
		}

		public float getVolume() {
			return this.mVolume;
		}

		public int getRepetitions() {
			return this.mRepetitions;
		}

		public boolean isLoad() {
			return this.mAction == SoundManager.SoundManagerTask.ACTION_LOAD;
		}

		public boolean isPlay() {
			return this.mAction == SoundManager.SoundManagerTask.ACTION_PLAY;
		}

		public boolean isUnload() {
			return this.mAction == SoundManager.SoundManagerTask.ACTION_UNLOAD;
		}

		public boolean isCancel() {
			return this.mAction == SoundManager.SoundManagerTask.ACTION_CANCEL;
		}

	}

}
