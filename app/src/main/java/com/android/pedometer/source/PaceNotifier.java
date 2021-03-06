package com.android.pedometer.source;

import java.util.ArrayList;

/**
 * Calculates and displays pace (steps / minute), handles input of desired pace,
 * notifies user if he/she has to go faster or slower.  
 * @author Levente Bagi
 */
public class PaceNotifier implements StepListener {

    public interface Listener {
        public void paceChanged(int value);
        public void passValue();
    }
    private ArrayList<Listener> mListeners = new ArrayList<Listener>();
    
    int mCounter = 0;
    
    private long mLastStepTime = 0;
    private long[] mLastStepDeltas = {-1, -1, -1, -1};
    private int mLastStepDeltasIndex = 0;
    private long mPace = 0;
    
    PedometerSettings mSettings;


    /** Desired pace, adjusted by the user */
    int mDesiredPace;

    /** Should we speak? */
    boolean mShouldTellFasterslower;

    /** When did the TTS speak last time */
    private long mSpokenAt = 0;

    public PaceNotifier(PedometerSettings settings, Utils mUtils) {
        mSettings = settings;
        mDesiredPace = mSettings.getDesiredPace();
        reloadSettings();
    }
    public void setPace(int pace) {
        mPace = pace;
        int avg = (int)(60*1000.0 / mPace);
        for (int i = 0; i < mLastStepDeltas.length; i++) {
            mLastStepDeltas[i] = avg;
        }
        notifyListener();
    }
    public void reloadSettings() {
        mShouldTellFasterslower = 
            mSettings.shouldTellFasterslower()
            && mSettings.getMaintainOption() == PedometerSettings.M_PACE;
        notifyListener();
    }
    
    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void setDesiredPace(int desiredPace) {
        mDesiredPace = desiredPace;
    }

    public void onStep() {
        long thisStepTime = System.currentTimeMillis();
        mCounter ++;
        
        // Calculate pace based on last x steps
        if (mLastStepTime > 0) {
            long delta = thisStepTime - mLastStepTime;
            
            mLastStepDeltas[mLastStepDeltasIndex] = delta;
            mLastStepDeltasIndex = (mLastStepDeltasIndex + 1) % mLastStepDeltas.length;
            
            long sum = 0;
            boolean isMeaningfull = true;
            for (int i = 0; i < mLastStepDeltas.length; i++) {
                if (mLastStepDeltas[i] < 0) {
                    isMeaningfull = false;
                    break;
                }
                sum += mLastStepDeltas[i];
            }
            if (isMeaningfull && sum > 0) {
                long avg = sum / mLastStepDeltas.length;
                mPace = 60*1000 / avg;
                
                // TODO: remove duplication. This also exists in SpeedNotifier
                if (mShouldTellFasterslower) {
                    if (thisStepTime - mSpokenAt > 3000) {
                        float little = 0.10f;
                        float normal = 0.30f;
                        float much = 0.50f;
                    }
                }
            }
            else {
                mPace = -1;
            }
        }
        mLastStepTime = thisStepTime;
        notifyListener();
    }
    
    private void notifyListener() {
        for (Listener listener : mListeners) {
            listener.paceChanged((int)mPace);
        }
    }
    
    public void passValue() {
        // Not used
    }

    

}

