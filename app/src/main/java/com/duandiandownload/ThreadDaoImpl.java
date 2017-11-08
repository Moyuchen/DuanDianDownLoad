package com.duandiandownload;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 张亚博
 * Date: 2017-11-08 11:29
 * Description：
 */
public class ThreadDaoImpl implements ThreadDao {
    private DBHelper dbHelper;

    public ThreadDaoImpl(Context context) {
        dbHelper = new DBHelper(context);
    }

    @Override
    public void addThreadInfo(ThreadInfo threadInfo) {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.execSQL("insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",new Object[]{
                threadInfo.getId(),threadInfo.getUrl(),threadInfo.getStart(),threadInfo.getEnd(),threadInfo.getFinished()});
        writableDatabase.close();
    }
    @Override
    public void deleteThreadInfo(String url, int thread_id) {
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.execSQL("delete from thread_info where url=?and thread_id=?", new Object[]{url,thread_id});
        writableDatabase.close();
    }
    @Override
    public void updateThreadInfo(String url, int thread_id,int finished) {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.execSQL("update thread_info set finished=? where url=?and thread_id=?", new Object[]{finished,url,thread_id});
        writableDatabase.close();
    }

    @Override
    public List<ThreadInfo> getThreadInfo(String url) {

        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        List<ThreadInfo>list = new ArrayList<>();

        Cursor cursor = readableDatabase.rawQuery("select * from thread_info where url=?",new String[]{url});
        while (cursor.moveToNext()){
            ThreadInfo thread  =new ThreadInfo();
            thread.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            thread.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            thread.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            thread.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            thread.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(thread);

        }

        readableDatabase.close();
        cursor.close();
        return list;
    }
    @Override
    public boolean findThreadInfo(String url, int thread_id) {
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        List<ThreadInfo>list = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("select * from thread_info where url=? and thread_id=?",new String[]{url,thread_id+""});
        boolean b = cursor.moveToNext();
        readableDatabase.close();
        cursor.close();
        return b;
    }
}
