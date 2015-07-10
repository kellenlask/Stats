package com.kellen.stats;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.lang.Math;

/**
 * Created by kellen on 7/9/15.
 */
public class ShakeDetector implements SensorEventListener {
//-------------------------------------------
//
//		Fields
//
//-------------------------------------------
	private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
	private static final int SHAKE_SLOP_TIME_MS = 500;
	private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

	private OnShakeListener shakeListener;
	private long shakeTimestamp;
	private int shakeCount;

//-------------------------------------------
//
//		OnShakeListener Interface
//
//-------------------------------------------
	public interface OnShakeListener {
		void onShake(int count);
	} //End public interface OnShakeListener

	public void setOnShakeListener(OnShakeListener listener) {
		this.shakeListener = listener;
	} //End public void setOnShakeListener(OnShakeListener)

//-------------------------------------------
//
//		Action Handlers
//
//-------------------------------------------
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		if (shakeListener != null) {
			float x = sensorEvent.values[0];
			float y = sensorEvent.values[1];
			float z = sensorEvent.values[2];

			float gX = x / SensorManager.GRAVITY_EARTH;
			float gY = y / SensorManager.GRAVITY_EARTH;
			float gZ = z / SensorManager.GRAVITY_EARTH;

			// gForce will be close to 1 when there is no movement.
			double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

			if (gForce > SHAKE_THRESHOLD_GRAVITY) {
				final long now = System.currentTimeMillis();
				// ignore shake events too close to each other (500ms)
				if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
					return;
				}

				// reset the shake count after 3 seconds of no shakes
				if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
					shakeCount = 0;
				}

				shakeTimestamp = now;
				shakeCount++;

				shakeListener.onShake(shakeCount);
			} //End inner-if
		} //End outer-if
	} //End public void onSensorChanged(SensorEvent)

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
		//Ignore
	} //End public void onAccuracyChanged(Sensor, int)
} //End class
