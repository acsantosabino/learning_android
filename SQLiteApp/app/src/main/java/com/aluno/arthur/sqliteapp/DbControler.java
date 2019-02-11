package com.aluno.arthur.sqliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbControler {
    private SQLiteDatabase db;
    private CriaBanco banco;

    public DbControler(Context context) {
        this.banco = new CriaBanco(context);
    }

    public String insereDados(String titulo, String autor, String editora){
        ContentValues values;
        long resultado;

        db = banco.getWritableDatabase();
        values = new ContentValues();
        values.put(CriaBanco.TITULO,titulo);
        values.put(CriaBanco.AUTOR,autor);
        values.put(CriaBanco.EDITORA,editora);

        resultado = db.insert(CriaBanco.TABELA,null,values);
        db.close();

        if (resultado < 0){
            return "Erro ao tentar inserir dado";
        } else {
            return "Dado "+resultado+" inserido com sucesso";
        }
    }

    public ArrayList<Livro> getAll(){
        ArrayList<Livro> livroList = new ArrayList<>();
        db = banco.getReadableDatabase();

        Cursor cursor = db.rawQuery( "SELECT  * FROM " + CriaBanco.TABELA,null);

        if ((cursor != null) && cursor.moveToFirst()) {
            do {
                Livro livro = new Livro();
                livro.setId(cursor.getInt(0));
                livro.setTitulo(cursor.getString(1));
                livro.setAutor(cursor.getString(2));
                livro.setEditora(cursor.getString(3));

                livroList.add(livro);
            } while (cursor.moveToNext());
        }

        db.close();
        return livroList;
    }

    public Livro getLivro(int id){
        Livro livro = new Livro();
        db = banco.getReadableDatabase();

        Cursor cursor = db.query(CriaBanco.TABELA,null,CriaBanco.ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
            livro.setId(cursor.getInt(0));
            livro.setTitulo(cursor.getString(1));
            livro.setAutor(cursor.getString(2));
            livro.setEditora(cursor.getString(3));
        }
        else
            livro = null;

        return livro;
    }

    public String delete(int id){
        db = banco.getWritableDatabase();
        int resulado;

        resulado = db.delete(CriaBanco.TABELA, CriaBanco.ID+" LIKE ?",
            new String[] {String.valueOf(id)});

        if (resulado > 0) {
            return resulado + "registros deletados com sucesso";
        }
        else
            return "Erro ao tentar deletar dado";
    }

    public String update(Livro livro){
        ContentValues values;
        int resultado;

        db = banco.getWritableDatabase();
        values = new ContentValues();
        values.put(CriaBanco.TITULO, livro.getTitulo());
        values.put(CriaBanco.AUTOR, livro.getAutor());
        values.put(CriaBanco.EDITORA, livro.getEditora());

        resultado = db.update(CriaBanco.TABELA, values,CriaBanco.ID+"=?",
            new String[] {String.valueOf(livro.getId())});
        db.close();

        if (resultado > 0){
            return resultado+ "registros atualizados com sucesso";
        } else {
            return resultado+": Erro ao tentar atualizar dado de "+livro.getId();
        }
    }
}
