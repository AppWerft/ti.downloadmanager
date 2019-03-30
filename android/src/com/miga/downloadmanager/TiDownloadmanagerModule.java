/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package com.miga.downloadmanager;

import org.appcelerator.kroll.KrollModule;

import android.net.ConnectivityManager;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.kroll.KrollDict;
import android.app.DownloadManager;
import android.app.Activity;
import android.net.Uri;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.io.TiBaseFile;
import org.appcelerator.titanium.io.TiFileFactory;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import org.appcelerator.kroll.KrollFunction;
import java.util.HashMap;
import android.content.Intent;
import android.database.Cursor;
import java.io.File;
import java.util.ArrayList;

@Kroll.module(name = "TiDownloadmanager", id = "com.miga.downloadmanager")
public class TiDownloadmanagerModule extends KrollModule {

	// Standard Debugging variables
	private static final String LCAT = "TiDownloadmanagerModule";
	private static final boolean DBG = TiConfig.LOGD;

	@Kroll.constant
	public static int COLUMN_REASON_PAUSED_QUEUED_FOR_WIFI = DownloadManager.PAUSED_QUEUED_FOR_WIFI;

	@Kroll.constant
	public static int COLUMN_REASON_PAUSED_UNKNOWN = DownloadManager.PAUSED_UNKNOWN;

	@Kroll.constant
	public static int COLUMN_REASON_PAUSED_WAITING_FOR_NETWORK = DownloadManager.PAUSED_WAITING_FOR_NETWORK;

	@Kroll.constant
	public static int COLUMN_REASON_PAUSED_WAITING_TO_RETRY = DownloadManager.PAUSED_WAITING_TO_RETRY;
	@Kroll.constant
	public static final int STATUS_FAILED = DownloadManager.STATUS_FAILED;
	@Kroll.constant
	public static final int STATUS_PAUSED = DownloadManager.STATUS_PAUSED;
	@Kroll.constant
	public static final int STATUS_PENDING = DownloadManager.STATUS_PENDING;
	@Kroll.constant
	public static final int STATUS_RUNNING = DownloadManager.STATUS_RUNNING;
	@Kroll.constant
	public static final int STATUS_SUCCESSFUL = DownloadManager.STATUS_SUCCESSFUL;

	public static final int STATUS_ALL = 0;

	@Kroll.constant
	public static final String COLUMN_BYTES_DOWNLOADED_SO_FAR = DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR;
	@Kroll.constant
	public static final String COLUMN_DESCRIPTION = DownloadManager.COLUMN_DESCRIPTION;
	@Kroll.constant
	public static final String COLUMN_ID = DownloadManager.COLUMN_ID;
	@Kroll.constant
	public static final String COLUMN_LAST_MODIFIED_TIMESTAMP = DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP;
	@Kroll.constant
	public static final String COLUMN_LOCAL_FILENAME = DownloadManager.COLUMN_LOCAL_FILENAME;
	@Kroll.constant
	public static final String COLUMN_LOCAL_URI = DownloadManager.COLUMN_LOCAL_URI;
	@Kroll.constant
	public static final String COLUMN_MEDIA_TYPE = DownloadManager.COLUMN_MEDIA_TYPE;
	@Kroll.constant
	public static final String COLUMN_MEDIAPROVIDER_URI = DownloadManager.COLUMN_MEDIAPROVIDER_URI;
	@Kroll.constant
	public static final String COLUMN_REASON = DownloadManager.COLUMN_REASON;
	@Kroll.constant
	public static final String COLUMN_STATUS = DownloadManager.COLUMN_STATUS;
	@Kroll.constant
	public static final String COLUMN_TITLE = DownloadManager.COLUMN_TITLE;
	@Kroll.constant
	public static final String COLUMN_TOTAL_SIZE_BYTES = DownloadManager.COLUMN_TOTAL_SIZE_BYTES;
	@Kroll.constant
	public static final String COLUMN_URI = DownloadManager.COLUMN_URI;
	@Kroll.constant
	public static final String ALLOWED_NETWORK_TYPES = "allowedNetworkTypes";

	private TiApplication appContext = TiApplication.getInstance();
	private Activity activity = appContext.getCurrentActivity();

	private DownloadManager dMgr;
	private KrollFunction callback;
	private int allowedNetworkTypes = ConnectivityManager.TYPE_MOBILE | ConnectivityManager.TYPE_WIFI | ConnectivityManager.TYPE_VPN;

	public TiDownloadmanagerModule() {
		super();
		ServiceReceiver service = new ServiceReceiver(this);
		activity.registerReceiver(service, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		activity.registerReceiver(service, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
		dMgr = (DownloadManager) appContext.getSystemService(appContext.DOWNLOAD_SERVICE);
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
	}

	// Methods
	@Kroll.method
	public void startDownload(KrollDict dict) {
		callback = (KrollFunction) dict.get("success");
		_startDownload(dict);
	}

	@Kroll.method
	public void setAllowedNetworkTypes(int allowedNetworkTypes) {
		this.allowedNetworkTypes = allowedNetworkTypes;
	}

	@Kroll.method
	public void remove(String id) {
		_remove(id);
	}

	@Kroll.method
	public String getMaxBytesOverMobile() {
		return "" + DownloadManager.getMaxBytesOverMobile(appContext);
	}

	@Kroll.method
	public String getRecommendedMaxBytesOverMobile() {
		return "" + DownloadManager.getRecommendedMaxBytesOverMobile(appContext);
	}

	@Kroll.method
	public Object[] getAllDownloads() {
		return _getDownloads(0);
	}

	@Kroll.method
	public Object[] getDownloads() {
		return _getDownloads(0);
	}

	@Kroll.method
	public Object[] getPendingDownloads() {
		return _getDownloads(DownloadManager.STATUS_PENDING);
	}

	@Kroll.method
	public Object[] getFailedDownloads() {
		return _getDownloads(DownloadManager.STATUS_FAILED);
	}

	@Kroll.method
	public Object[] getPausedDownloads() {
		return _getDownloads(DownloadManager.STATUS_PAUSED);
	}

	@Kroll.method
	public Object[] getRunningDownloads() {
		return _getDownloads(DownloadManager.STATUS_RUNNING);
	}

	@Kroll.method
	public Object[] getSuccessfulDownloads() {
		return _getDownloads(DownloadManager.STATUS_SUCCESSFUL);
	}

	private Object[] _getDownloads(int status) {
		@SuppressWarnings("rawtypes")
		ArrayList<HashMap> downList = new ArrayList<HashMap>();
		DownloadManager.Query query = new DownloadManager.Query();
		if (dMgr == null) {
			dMgr = (DownloadManager) appContext.getSystemService(appContext.DOWNLOAD_SERVICE);
			return downList.toArray();
		}

		Cursor c = dMgr.query(query);
		c.moveToFirst();
		while (c.moveToNext()) {
			HashMap<String, Object> dl = new HashMap<String, Object>();

			String filename = null;
			String downloadFileLocalUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
			if (downloadFileLocalUri != null) {
				File mFile = new File(Uri.parse(downloadFileLocalUri).getPath());
				filename = mFile.getAbsolutePath();
			}

			int bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
			int bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

			dl.put("status", c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)));
			dl.put("filename", filename);
			dl.put("size_total", bytes_total);
			dl.put("size_downloaded", bytes_downloaded);
			dl.put("reason", c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON)));
			dl.put("title", c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE)));
			dl.put("mediatype", c.getString(c.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE)));
			if (status == STATUS_ALL) {
				downList.add(dl);
			}
			if (status == c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
				downList.add(dl);
			}

		}
		c.close();
		return downList.toArray();
	}

	public void done() {
		if (callback != null) {
			HashMap<String, String> event = new HashMap<String, String>();
			// event.put("something","something");
			callback.call(getKrollObject(), event);
		}
	}

	public void cancel() {
		Intent pageView = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
		pageView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		appContext.startActivity(pageView);
	}

	private void _remove(String id) {

	}

	private void _startDownload(KrollDict dict) {
		DownloadManager.Request dmReq = new DownloadManager.Request(Uri.parse(TiConvert.toString(dict, "url")));
		dmReq.setTitle(TiConvert.toString(dict, "title"));
		dmReq.setDescription(TiConvert.toString(dict, "description"));
		
		if (dict.containsKeyAndNotNull(ALLOWED_NETWORK_TYPES)) {
			dmReq.setAllowedNetworkTypes(dict.getInt(ALLOWED_NETWORK_TYPES));
		} else {
			dmReq.setAllowedNetworkTypes(allowedNetworkTypes);
		}
		if (dict.containsKeyAndNotNull("allowedOverMetered")) {
			dmReq.setAllowedOverMetered(dict.getBoolean("allowedOverMetered"));
		}
		if (dict.containsKeyAndNotNull("allowedOverRoaming")) {
			dmReq.setAllowedOverRoaming(dict.getBoolean("allowedOverRoaming"));
		}
		Log.i(LCAT, "Download to " + TiConvert.toString(dict, "filename"));
		TiBaseFile file = TiFileFactory.createTitaniumFile(new String[] { TiConvert.toString(dict, "filename") },
				false);
		dmReq.setDestinationUri(Uri.fromFile(file.getNativeFile()));
		dMgr.enqueue(dmReq);
	}

}
