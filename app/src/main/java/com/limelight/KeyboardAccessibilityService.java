package com.limelight;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/**
 * @author hanbo
 * @since 2024/1/16
 */
public class KeyboardAccessibilityService extends AccessibilityService {
    private static final List BLACKLIST_KEYS = Arrays.asList(
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_POWER,
            KeyEvent.KEYCODE_HOME
    );

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
    }

    @Override
    public void onInterrupt() {
    }

    private int escNum = 0;
    private long escClickTime = 0;

    @Override
    public boolean onKeyEvent(KeyEvent keyEvent) {
        int action = keyEvent.getAction();
        int keyCode = keyEvent.getKeyCode();
        if (action == KeyEvent.ACTION_DOWN) {
            Log.i("hh","KeyboardAccessibilityService press keyevent-->" + keyEvent);
        }

        Game game = Game.instance;
        if (game != null && game.connected && !BLACKLIST_KEYS.contains(Integer.valueOf(keyCode))) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                keyEvent = new KeyEvent(keyEvent.getDownTime(), keyEvent.getEventTime(), keyEvent.getAction(),
                        KeyEvent.KEYCODE_ESCAPE, keyEvent.getRepeatCount(), keyEvent.getMetaState(), keyEvent.getDeviceId(),
                        keyEvent.getScanCode(), keyEvent.getFlags(), keyEvent.getSource());
            }
            if (action == KeyEvent.ACTION_DOWN) {
                Game.instance.handleKeyDown(keyEvent);
                return true;
            } else if (action == KeyEvent.ACTION_UP) {
                Game.instance.handleKeyUp(keyEvent);
                return true;
            }
        }
        return super.onKeyEvent(keyEvent);
    }

   /* private KeyEvent processBack(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() != KeyEvent.KEYCODE_BACK) {
            return keyEvent;
        }
        //按下处理
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            long sc = SystemClock.uptimeMillis();
            if ((sc - escClickTime) < 800) {
                escNum++;
                //Toast.makeText(Game.instance, "click 3 times will back。", Toast.LENGTH_SHORT).show(); //fixme
            } else {
                escClickTime = sc;
                escNum = 1;
            }
        }

        if (escNum < 3) {
            Log.i("hh", "esc事件-->" + keyEvent.getAction());
            return new KeyEvent(keyEvent.getDownTime(), keyEvent.getEventTime(), keyEvent.getAction(),
                    KeyEvent.KEYCODE_ESCAPE, keyEvent.getRepeatCount(), keyEvent.getMetaState(), keyEvent.getDeviceId(),
                    keyEvent.getScanCode(), keyEvent.getFlags(), keyEvent.getSource());
        } else {
            Log.i("hh", "后退事件-->" + keyEvent.getAction());
            return new KeyEvent(keyEvent.getDownTime(), keyEvent.getEventTime(), keyEvent.getAction(),
                    KeyEvent.KEYCODE_BACK, keyEvent.getRepeatCount(), keyEvent.getMetaState(), keyEvent.getDeviceId(),
                    keyEvent.getScanCode(), keyEvent.getFlags(), keyEvent.getSource());
        }
    }*/

   /* 已在xml配置@Override
    public void onServiceConnected() {
        super.onServiceConnected();
        Log.i("hh", "Keyboard service is connected");
                AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.packageNames = new String[]{BuildConfig.APPLICATION_ID};
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.notificationTimeout = 100;
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        setServiceInfo(info);
    }*/
}