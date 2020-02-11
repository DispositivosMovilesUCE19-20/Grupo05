package ec.edu.uce.optativa3;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlLite extends SQLiteOpenHelper{
    public SqlLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase Op3) {
        Op3.execSQL("create table USUARIOS( usuario text primary key, password text)");
        Op3.execSQL("create table ESTUDIANTES( " +
                "id  integer primary key AUTOINCREMENT,nombre text , apellido text, email text , celular text, genero text, fecha date , asignaturas text, becado text,foto text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
