package com.dlut.tec2ksim;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class InstructionDB {

    public static final String path = "/data/data/com.dlut.tec2ksim/files/AssemblyInstruction.db";

    /**
     * 返回一共有多少个分组
     *
     * @return
     */
    public static int getGroupCount() {
        int count = 0;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor curosr = db.rawQuery("select * from instruction", null);
        count = curosr.getCount();
        curosr.close();
        db.close();
        return count;
    }

    /**
     * 返回instructions
     *
     * @return
     */
    public static List<String> getInstructions() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor curosr = db.rawQuery("select instruction from instruction", null);
        List<String> instructions = new ArrayList<String>();
        while (curosr.moveToNext()) {
            String instruction = curosr.getString(0);
            instructions.add(instruction);
        }
        curosr.close();
        db.close();
        return instructions;
    }


    /**
     * 返回explanations
     *
     * @return
     */
    public static List<String> getExplanations() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor curosr = db.rawQuery("select explanation from instruction", null);
        List<String> explanations = new ArrayList<String>();
        while (curosr.moveToNext()) {
            String explanation = curosr.getString(0);
            explanations.add(explanation);
        }
        curosr.close();
        db.close();
        return explanations;

    }

    /**
     * 返回instructions
     *
     * @return
     */
    public static List<String> getSearchInstruction(String s) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor curosr = db.rawQuery("select instruction from instruction where instruction like" + "'%" + s + "%'", null);
        List<String> instructions = new ArrayList<String>();
        while (curosr.moveToNext()) {
            String instruction = curosr.getString(0);
            instructions.add(instruction);
        }
        curosr.close();
        db.close();
        return instructions;
    }

    /**
     * search
     *
     * @return
     */
    public static List<String> getSearchExplanation(String s) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor curosr = db.rawQuery("select explanation from instruction where instruction like" + "'%" + s + "%'", null);
        List<String> explanations = new ArrayList<String>();
        while (curosr.moveToNext()) {
            String explanation = curosr.getString(0);
            explanations.add(explanation);
        }
        curosr.close();
        db.close();
        return explanations;

    }

    /**
     * getInstructionMore
     *
     * @return
     */
    public static List<String> getInstructionMore(String s) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor curosr = db.rawQuery("select * from instruction where instruction =" + "'" + s + "'", null);
        List<String> explanationMore = new ArrayList<String>();

        curosr.moveToNext();
//		Log.e("mr", "select * from instruction where instruction ="+"'"+s+"'");
//		Log.e("mr", "count:"+curosr.getCount());
        for (int i = 0; i < 6; i++) {
            String explanation = curosr.getString(i);
//			Log.e("mr", "explanation:"+explanation);
            explanationMore.add(explanation);
        }
        curosr.close();
        db.close();
        return explanationMore;

    }

    /**
     * example
     *
     * @return
     */
    public static List<String> getExampleTitles() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor curosr = db.rawQuery("select title from example", null);
        List<String> explanations = new ArrayList<String>();
        int i = 1;
        while (curosr.moveToNext()) {
            String explanation = "案例" + i + "：" + curosr.getString(0);
            explanations.add(explanation);
            i++;
        }
        curosr.close();
        db.close();
        return explanations;

    }

    /**
     * SpecificExample
     *
     * @return
     */
    public static List<String> getSpecificExample(int position) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor curosr = db.rawQuery("select * from example where no =" + "'" + position + "'", null);
        List<String> specificExamples = new ArrayList<String>();

        curosr.moveToNext();

//		Log.e("mr", "select * from example where no ="+"'"+position+"'");
//		Log.e("mr", "count:"+curosr.getColumnCount());

        for (int i = 0; i < curosr.getColumnCount(); i++) {
            String specificExample = curosr.getString(i);
//			Log.e("mr", "explanation:"+specificExample);
            specificExamples.add(specificExample);
        }
        curosr.close();
        db.close();
        return specificExamples;

    }
}
