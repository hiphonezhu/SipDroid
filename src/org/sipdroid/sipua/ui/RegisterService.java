/*
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * 
 * This file is part of Sipdroid (http://www.sipdroid.org)
 * 
 * Sipdroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.sipdroid.sipua.ui;

import org.sipdroid.media.RtpStreamReceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;

public class RegisterService extends Service {
	Receiver m_receiver;
	Caller m_caller;
	
    public void onDestroy() {
		super.onDestroy();
		if (m_receiver != null) {
			unregisterReceiver(m_receiver);
			m_receiver = null;
		}
		Receiver.alarm(0, OneShotAlarm2.class);
	}
    
    @Override
    public void onCreate() {
    	super.onCreate();
    	if (Receiver.mContext == null) Receiver.mContext = this;
    	/**
    	 * 注册Receiver接受广播（系统完成启动广播、网络连接的变化广播、Vpn 连接变化广播、数据状态改变广播、电话状态改变广播、
    	 * 亮度传感器广播、蓝牙SCO音频连接状态已改变广播、有线耳机插入或拔出广播、手机屏幕锁屏和解锁广播、用户被唤醒触发广播、wiif状态改变广播）
    	 */
        if (m_receiver == null) {
			 IntentFilter intentfilter = new IntentFilter();
			 intentfilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			 intentfilter.addAction(Receiver.ACTION_DATA_STATE_CHANGED);
			 intentfilter.addAction(Receiver.ACTION_PHONE_STATE_CHANGED);
			 intentfilter.addAction(Receiver.ACTION_DOCK_EVENT);
			 intentfilter.addAction(Intent.ACTION_HEADSET_PLUG);
			 intentfilter.addAction(Intent.ACTION_USER_PRESENT);
			 intentfilter.addAction(Intent.ACTION_SCREEN_OFF);
			 intentfilter.addAction(Intent.ACTION_SCREEN_ON);
			 intentfilter.addAction(Receiver.ACTION_VPN_CONNECTIVITY);
			 intentfilter.addAction(Receiver.ACTION_SCO_AUDIO_STATE_CHANGED);
			 intentfilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
			 intentfilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
	         registerReceiver(m_receiver = new Receiver(), intentfilter);      
	         intentfilter = new IntentFilter();
        }
        Receiver.engine(this).isRegistered();
        RtpStreamReceiver.restoreSettings();
    }
    
    @Override
    public void onStart(Intent intent, int id) {
         super.onStart(intent,id);
         Receiver.alarm(10*60, OneShotAlarm2.class);
    }

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
}
