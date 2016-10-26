package br.edu.ifspsaocarlos.agenda.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import java.util.ArrayList;
import java.util.List;


public class ContatoDAO {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public ContatoDAO(Context context) {
        this.dbHelper=new SQLiteHelper(context);
    }

    public  List<Contato> buscaContato(String nome)
    {
        database=dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL};
        String where=null;
        String[] argWhere=null;

        if (nome!=null) {
            where = SQLiteHelper.KEY_NAME + " like ?";
            argWhere = new String[]{nome + "%"};
        }


        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);



       if (cursor!=null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Contato contato = new Contato();
                contato.setId(cursor.getInt(0));
                contato.setNome(cursor.getString(1));
                contato.setFone(cursor.getString(2));
                contato.setEmail(cursor.getString(3));
                contatos.add(contato);
                cursor.moveToNext();
            }
            cursor.close();
        }
        database.close();
        return contatos;
    }

    public void atualizaContato(Contato c) {
        database=dbHelper.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put(SQLiteHelper.KEY_NAME, c.getNome());
        updateValues.put(SQLiteHelper.KEY_FONE, c.getFone());
        updateValues.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
        database.update(SQLiteHelper.DATABASE_TABLE, updateValues, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        database.close();
    }


    public void insereContato(Contato c) {
        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_FONE, c.getFone());
        values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
        database.insert(SQLiteHelper.DATABASE_TABLE, null, values);
        database.close();
    }

    public void apagaContato(Contato c)
    {
        database=dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        database.close();
    }
}
