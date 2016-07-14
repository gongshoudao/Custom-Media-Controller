package com.androidcycle.custommediacontroller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private PhoneWindowMediaController mMediaController;

//    private MyMediaController mMediaController;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ViewGroup root = (ViewGroup) view.findViewById(R.id.controller_root);
        View root = view.findViewById(R.id.content_main);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    toggleControlsVisibility();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.performClick();
                }
                return true;
            }
        });
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE
                        || keyCode == KeyEvent.KEYCODE_MENU) {
                    return false;
                }
                return mMediaController.dispatchKeyEvent(event);
            }
        });
        mMediaController = new PhoneWindowMediaController(getActivity());
//        mMediaController = new MyMediaController(getActivity());
        mMediaController.setAnchorView(root);
        mMediaController.setMediaPlayer(new MediaController.MediaPlayerControl() {
            private int currentPosition;
            boolean isPlaying = false;

            @Override
            public void start() {
                Log.i(TAG, "start: ");
                isPlaying = true;
            }

            @Override
            public void pause() {
                Log.i(TAG, "pause: ");
                isPlaying = false;
            }

            @Override
            public int getDuration() {
                Log.i(TAG, "getDuration: ");
                return 6640130;
            }

            @Override
            public int getCurrentPosition() {
                Log.i(TAG, "getCurrentPosition: ");
                return currentPosition;
            }

            @Override
            public void seekTo(int pos) {
                Log.i(TAG, "seekTo: ");
                currentPosition = pos;
            }

            @Override
            public boolean isPlaying() {
                Log.i(TAG, "isPlaying: ");
                return isPlaying;
            }

            @Override
            public int getBufferPercentage() {
                Log.i(TAG, "getBufferPercentage: ");
                return 50;
            }

            @Override
            public boolean canPause() {
                Log.i(TAG, "canPause: ");
                return true;
            }

            @Override
            public boolean canSeekBackward() {
                Log.i(TAG, "canSeekBackward: ");
                return true;
            }

            @Override
            public boolean canSeekForward() {
                Log.i(TAG, "canSeekForward: ");
                return true;
            }

            @Override
            public int getAudioSessionId() {
                Log.i(TAG, "getAudioSessionId: ");
                return 0;
            }
        });
        mMediaController.setEnabled(true);
    }

    private void toggleControlsVisibility() {
        if (mMediaController.isShowing()) {
            mMediaController.hide();
        } else {
            showControls();
        }
    }

    private void showControls() {
        mMediaController.show(0);
    }
}
