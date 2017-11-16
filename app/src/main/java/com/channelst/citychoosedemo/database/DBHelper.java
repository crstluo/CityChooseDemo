package com.channelst.citychoosedemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author luojingjing 用法： DBHelper dbHelper = new DBHelper(this);
 *         dbHelper.createDataBase(); SQLiteDatabase db =
 *         dbHelper.getWritableDatabase(); Cursor cursor = db.query()
 *         db.execSQL(sqlString); 注意：execSQL不支持带;的多条SQL语句，只能一条一条的执行，晕了很久才明白
 *         见execSQL的源码注释 (Multiple statements separated by ;s are not
 *         supported.) 将把assets下的数据库文件直接复制到DB_PATH，但数据库文件大小限制在1M以下
 *         如果有超过1M的大文件，则需要先分割为N个小文件，然后使用copyBigDatabase()替换copyDatabase()
 */
public class DBHelper extends SQLiteOpenHelper {
	// 用户数据库文件的版本
	private static final int DB_VERSION = 3;
	// 数据库文件目标存放路径为系统默认位置，com.channelst.citychoosedemo 是你的包名
	private static String DB_PATH = "/data/data/com.channelst.citychoosedemo/databases/";

	private static String DB_NAME = "meituan_cities.db";
	private static String ASSETS_NAME = "meituan_cities.db";

	private SQLiteDatabase mDataBase = null;
	private final Context mContext;


	/**
	 * 在SQLiteOpenHelper的子类当中，必须有该构造函数
	 * @param context 上下文对象
	 * @param name 数据库名称
	 * @param factory 一般都是null
	 * @param version 当前数据库的版本，值必须是整数并且是递增的状态
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
                    int version) {
		// 必须通过super调用父类当中的构造函数
		super(context, name, null, version);
		this.mContext = context;
	}

	public DBHelper(Context context) {
		this(context, DB_PATH + DB_NAME, null, DB_VERSION);
	}

	/**
	 * 创建数据库
	 * 1.检查数据库文件是否已经存在,不存在则拷贝数据库文件到指定路径保存
	 * @throws IOException
	 */
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			// 创建数据库
			try {
				File dir = new File(DB_PATH);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File dbf = new File(DB_PATH + DB_NAME);
				if (dbf.exists()) {
					dbf.delete();
				}
				SQLiteDatabase.openOrCreateDatabase(dbf, null);
				// 复制asseets中的db文件到DB_PATH下
				copyDataBase();
			} catch (IOException e) {
				throw new Error("数据库创建失败");
			}
		}
	}

	/**
	 * 检查数据库是否有效
 	 */
	private boolean checkDataBase() {
		String myPath = DB_PATH + DB_NAME;
		File dbFile = new File(myPath);
		if (!dbFile.exists()) {
			return false;
		}
		return true;
	}

	/**
	 * 从assets目录下拷贝数据库文件到指定路径
	 * */
	private void copyDataBase() throws IOException {
		InputStream myInput = mContext.getAssets().open(ASSETS_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}


	@Override
	public synchronized void close() {
		if (mDataBase != null) {
			mDataBase.close();
		}
		super.close();
	}

	/**
	 * 该函数是在第一次创建的时候执行， 实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	/**
	 * 数据库表结构有变化时采用
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}