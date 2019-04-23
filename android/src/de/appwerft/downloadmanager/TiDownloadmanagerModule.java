/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package de.appwerft.downloadmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiFileProxy;
import org.appcelerator.titanium.io.TiBaseFile;
import org.appcelerator.titanium.io.TiFileFactory;
import org.appcelerator.titanium.util.TiConvert;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import ti.modules.titanium.filesystem.FileProxy;

@Kroll.module(name = "TiDownloadmanager", id = "de.appwerft.downloadmanager", propertyAccessors = {
		Constants.PROPERTY_EVENT_ONDONE, Constants.PROPERTY_EVENT_ONCOMPLETE })
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
	@Kroll.constant
	public static final int ERROR_CANNOT_RESUME = DownloadManager.ERROR_CANNOT_RESUME;
	@Kroll.constant
	public static final int ERROR_DEVICE_NOT_FOUND = DownloadManager.ERROR_DEVICE_NOT_FOUND;
	@Kroll.constant
	public static final int ERROR_FILE_ALREADY_EXISTS = DownloadManager.ERROR_FILE_ALREADY_EXISTS;
	@Kroll.constant
	public static final int ERROR_FILE_ERROR = DownloadManager.ERROR_FILE_ERROR;
	@Kroll.constant
	public static final int ERROR_HTTP_DATA_ERROR = DownloadManager.ERROR_HTTP_DATA_ERROR;
	@Kroll.constant
	public static final int ERROR_INSUFFICIENT_SPACE = DownloadManager.ERROR_INSUFFICIENT_SPACE;
	@Kroll.constant
	public static final int ERROR_TOO_MANY_REDIRECTS = DownloadManager.ERROR_TOO_MANY_REDIRECTS;
	@Kroll.constant
	public static final int ERROR_UNHANDLED_HTTP_CODE = DownloadManager.ERROR_UNHANDLED_HTTP_CODE;
	@Kroll.constant
	public static final int ERROR_UNKNOWN = DownloadManager.ERROR_UNKNOWN;

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
	private static TiApplication tiapp;
	private Activity activity = appContext.getCurrentActivity();

	public static DownloadManager dMgr;
	private KrollFunction callback;
	private String eventName = "DownloadReady";
	private int notificationvisibility = 0;

	private int allowedNetworkTypes = ConnectivityManager.TYPE_MOBILE | ConnectivityManager.TYPE_WIFI
			| ConnectivityManager.TYPE_VPN;

	public TiDownloadmanagerModule() {
		super();
		ServiceReceiver service = new ServiceReceiver(this);
		activity.registerReceiver(service, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		activity.registerReceiver(service, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
		dMgr = (DownloadManager) appContext.getSystemService(Context.DOWNLOAD_SERVICE);
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		tiapp = app;
	}

	@Kroll.method
	public Long startDownload(KrollDict dict) {
		if (dict.containsKeyAndNotNull("success")) {
			Object o = dict.get("success");
			if (o instanceof KrollFunction) {
				callback = (KrollFunction) dict.get("success");
				Log.d(LCAT, "success callback successfull registered");
			} else
				Log.w(LCAT, "success isn't a callback");
		} else
			Log.w(LCAT, "missing success property");
		if (dict.containsKeyAndNotNull(TiC.PROPERTY_URL)) {
			try {
				new URI(dict.getString(TiC.PROPERTY_URL));
				return _startDownload(dict);
			} catch (URISyntaxException e) {
				Log.e(LCAT, "url is not valid");
				return new Long(0);
			}
		}
		return new Long(0);
	}

	@Kroll.method
	public void setAllowedNetworkTypes(int allowedNetworkTypes) {
		this.allowedNetworkTypes = allowedNetworkTypes;
	}

	@Kroll.method
	public Long getMaxBytesOverMobile() {
		return DownloadManager.getMaxBytesOverMobile(appContext);
	}

	@Kroll.method
	public String getMimeTypeForDownloadedFile(Long id) {
		return getMimeTypeForDownloadedFile(id);
	}

	@Kroll.method
	public Long getRecommendedMaxBytesOverMobile() {
		return DownloadManager.getRecommendedMaxBytesOverMobile(appContext);
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

	@Kroll.method
	public KrollDict getStatusOfDownload(String url) {
		KrollDict res = new KrollDict();

		DownloadManager.Query query = new DownloadManager.Query();
		if (dMgr == null) {
			res.put("error", "no DownloadManager");
			return res;
		}
		Cursor c = dMgr.query(query);
		c.moveToFirst();
		while (c.moveToNext()) {
			if (url.equals(c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI)))) {
				res.put("status", c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)));
			}
		}
		if (res.containsKey("status"))
			res.put("error", "url not in DownloadManager");
		c.close();
		return res;
	}

	@Kroll.method
	public Object[] getDownloadById(Object o) {
		if (o instanceof Long) {

			long[] ids = { (long) o };
			return _getDownloadsByIds(ids);
		} else {
			Log.w(LCAT, "getDownloadById() aspects long, but got " + o.getClass().getSimpleName());
			return null;
		}
	}

	@Kroll.method
	public Object[] getDownloadsByIds(Object args) {
		return _getDownloadsByIds(importLongList(args));
	}

	@SuppressWarnings("rawtypes")
	private Object[] _getDownloadsByIds(long[] ids) {
		getInstance();
		ArrayList<HashMap> downList = new ArrayList<HashMap>();
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById();
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
		}
		c.close();
		return downList.toArray();
	}

	private Object[] _getDownloads(int status) {
		@SuppressWarnings("rawtypes")
		ArrayList<HashMap> downList = new ArrayList<HashMap>();
		DownloadManager.Query query = new DownloadManager.Query();
		getInstance();
		Cursor c = dMgr.query(query);
		c.moveToFirst();
		while (c.moveToNext()) {
			HashMap<String, Object> dl = new HashMap<String, Object>();
			String filename = null;
			TiFileProxy fileproxy =null;
			String downloadFileLocalUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
			if (downloadFileLocalUri != null) {
				File mFile = new File(Uri.parse(downloadFileLocalUri).getPath());
				filename = mFile.getAbsolutePath();
				fileproxy = new TiFileProxy(TiFileFactory.createTitaniumFile(new String[] { downloadFileLocalUri }, false));
			}
			int bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
			int bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
			dl.put("status", c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)));
			dl.put("filename", filename);
			dl.put("file", fileproxy);
			
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

	/* these 3 method will called from ServiceReceiver */
	public void done(Long id) {
		KrollDict event = new KrollDict();
		event.put("id", id);
		/* sends an event to tiapp, every part of app can receive */
		tiapp.fireAppEvent("downloadmanager.done", event);
		/* send to instances of module, (require('de.appwert.downloadmanager')) */
		sendBack(event, Constants.PROPERTY_EVENT_ONDONE);
	}

	public void complete() {
		KrollDict event = new KrollDict();
		tiapp.fireAppEvent("downloadmanager..complete", event);
		sendBack(event, Constants.PROPERTY_EVENT_ONCOMPLETE);
	}

	public void cancel() {
		Intent pageView = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
		pageView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		appContext.startActivity(pageView);
	}

	@Kroll.method
	public Long enqueue(RequestProxy proxy) {
		return dMgr.enqueue(proxy.request);
	}

	/* Titaniums Javascript uses an array of long */
	/* API aspects Long... */
	@Kroll.method
	public int removeDownloadById(Object o) {
		/* Titanium give us one long id: */
		if (o instanceof Long) {
			return dMgr.remove((Long) o);
		} else {
			Log.w(LCAT,"removeDownloadById() aspects an id (number)" );
			return 0;
		}
	}
	
	@Kroll.method 
	public TiFileProxy getTiFileForDownloadedFile(Long id) {
		Uri uri= dMgr.getUriForDownloadedFile(id);
		return new TiFileProxy(TiFileFactory.createTitaniumFile(new String[] { uri.getPath() }, false));
	} 
	@Kroll.method
	public int removeDownloadsByIds(Object o) {
		long[] ids = importLongList(o);
		if (ids != null)
			return dMgr.remove(ids);
		return 0;
	}

	private Long _startDownload(KrollDict dict) {
		DownloadManager.Request dmReq = new DownloadManager.Request(Uri.parse(dict.getString(TiC.PROPERTY_URL)));
		dmReq.setTitle(TiConvert.toString(dict, "title"));
		dmReq.setDescription(TiConvert.toString(dict, "description"));
		if (dict.containsKeyAndNotNull("notificationvisible"))

			// https://stackoverflow.com/questions/9345977/downloadmanager-request-setnotificationvisibility-fails-with-jsecurityexception

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
		return dMgr.enqueue(dmReq);
	}

	/* Titanium calls a function on JS layer */
	private void sendBack(KrollDict event, String prop) {
		if (hasProperty(prop)) {
			Object o = getProperty(prop);
			if (o instanceof KrollFunction) {
				((KrollFunction) o).callAsync(getKrollObject(), event);
			}
		}
	}

	private void getInstance() {
		if (dMgr == null) {
			dMgr = (DownloadManager) appContext.getSystemService(Context.DOWNLOAD_SERVICE);
		}
	}

	private long[] importLongList(Object arg) {
		int ndx = 0;
		long[] longlist = null;

		if (arg instanceof Long) {
			longlist = new long[1];
			longlist[0] = (Long) arg;
		} else if (arg instanceof Object[]) {
			longlist = new long[Array.getLength(arg)];
			for (Object e : (Object[]) arg) {
				if (e instanceof Long) {
					longlist[ndx] = (Long) e;
					ndx++;
				}
			}
		}
		return longlist;
	}
	
}

	
	

	
	
	
	

