package cn.news.ziri.newsaggregation.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import cn.news.ziri.newsaggregation.activity.MainActivity;
import cn.news.ziri.newsaggregation.commons.Urls;
import cn.news.ziri.newsaggregation.utils.Logziri;

/**
 * Created by ward on 2017/8/3.
 * Email：nuaaduwei@126.com
 */

public class NewsSourceSQLiteOpenHelper extends SQLiteOpenHelper {
    /**
     *
     * @param context 环境变量
     * @param name 数据库名字
     * @param factory 返回的游标
     * @param version 版本你控制
     */
    public NewsSourceSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

/*可以用这样的方法来实现快速插入多条数据*/
//String sql = "INSERT INTO table (number, nick) VALUES (?, ?)";
//        db.beginTransaction();
//        SQLiteStatement stmt = db.compileStatement(sql);
//        for (int i = 0; i < values.size(); i++) {
//            stmt.bindString(1, values.get(i).number);
//            stmt.bindString(2, values.get(i).nick);
//            stmt.execute();
//            stmt.clearBindings();
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE newssource (id integer primary key autoincrement,name varchar(20),uri varchar(100),isneedjson integer,isselected integer)");
        Logziri.d(getClass()+"onCreate");
        db.beginTransaction();//开启事物
        db.execSQL("insert into newssource (name,uri,isneedjson,isselected) values(\"网易新闻\",\"\",1,1)");
        db.execSQL("insert into newssource (name,uri,isneedjson,isselected) values(\"凤凰新闻\",\"http://i.ifeng.com/\",0,1)");
        db.execSQL("insert into newssource (name,uri,isneedjson,isselected) values(\"头条新闻\",\"https://m.toutiao.com/\",0,0)");
        db.execSQL("insert into newssource (name,uri,isneedjson,isselected) values(\"环球新闻\",\"http://m.huanqiu.com/\",0,1)");
        db.execSQL("insert into newssource (name,uri,isneedjson,isselected) values(\"人民网\",\"http://m.people.cn/\",0,1)");
        db.execSQL("insert into newssource (name,uri,isneedjson,isselected) values(\"参考消息\",\"http://m.cankaoxiaoxi.com/\",0,1)");
        db.execSQL("insert into newssource (name,uri,isneedjson,isselected) values(\"南方周末\",\"http://m.infzm.com/\",0,1)");
        db.execSQL("insert into newssource (name,uri,isneedjson,isselected) values(\"经济日报\",\"http://m.ce.cn/yw/\",0,1)");
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
