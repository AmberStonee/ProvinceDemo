package com.jkxy.provincedemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

@SuppressLint("SdCardPath")
public class MainActivity extends ActionBarActivity {

	private static final String databasepath = "/data/data/com.jkxy.provincedemo/";

	private static final String PROVINCES = "provinces";
	private static final String CITYS = "citys";

	private SQLiteDatabase dataBase;

	private Map<String, String> provinces;

	private Spinner sp_pro, sp_city;

	private SpAdapter adapter_pro, adapter_city;

	private List<String> citys = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		boolean flag = copyDataToPhone(databasepath + "db_weather.db");
		File f = new File(databasepath + "db_weather.db");
		if (f.exists() && f.isFile()) {
			dataBase = this.openOrCreateDatabase(
					databasepath + "db_weather.db", Context.MODE_PRIVATE, null);
		}
		provinces = initProvinces();
		setAdapter();
		setListener();
	}

	private void initView() {
		sp_pro = (Spinner) findViewById(R.id.sp_pro);
		sp_city = (Spinner) findViewById(R.id.sp_city);
	}

	private String getId(String name) {
		Set<String> keySet = provinces.keySet();
		String target = null;
		for (String key : keySet) {
			String string = provinces.get(key);
			if (string.equals(name)) {
				target = key;
			}
		}

		target = String.valueOf(Integer.valueOf(target) - 1);

		return target;
	}

	private void setListener() {

		sp_pro.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				String name = (String) view.getTag();

				String searchId = getId(name);

				citys = getCitys(searchId);
				adapter_city = new SpAdapter(MainActivity.this, citys);
				sp_city.setAdapter(adapter_city);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

	}

	private void setAdapter() {
		Set<String> keySet = provinces.keySet();
		List<String> proNames = new ArrayList<String>();
		for (String string : keySet) {
			proNames.add(provinces.get(string));
		}
		adapter_pro = new SpAdapter(this, proNames);
		citys = getCitys("0");
		adapter_city = new SpAdapter(this, citys);
		sp_city.setAdapter(adapter_city);
		sp_pro.setAdapter(adapter_pro);
	}

	private Map<String, String> initProvinces() {
		provinces = new HashMap<String, String>();

		Cursor query = dataBase.query(PROVINCES, new String[] { "_id,name" },
				null, null, null, null, null, null);
		while (query.moveToNext()) {
			provinces.put(query.getString(query.getColumnIndex("_id")),
					query.getString(query.getColumnIndex("name")));
		}
		return provinces;
	}

	private List<String> getCitys(String proId) {
		List<String> citys = new ArrayList<String>();
		Cursor query = dataBase.query(CITYS, new String[] { "name" },
				"province_id=?", new String[] { proId }, null, null, null);
		while (query.moveToNext()) {
			String string = query.getString(0);
			citys.add(string);
		}
		return citys;
	}

	private boolean copyDataToPhone(String path) {
		AssetManager assets = getAssets();
		InputStream open = null;
		FileOutputStream fos = null;
		try {
			open = assets.open("db_weather.db");
			byte buffer[] = new byte[100];
			fos = new FileOutputStream(path);
			int length = 0;
			while ((length = open.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			fos.flush();
			fos.close();
			open.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			try {
				if (open != null) {
					open.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		return false;
	}
}
