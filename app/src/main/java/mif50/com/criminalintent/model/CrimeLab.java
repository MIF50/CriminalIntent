package mif50.com.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mif50.com.criminalintent.database.CrimeBaseHelper;
import mif50.com.criminalintent.database.CrimeCursorWrapper;
import mif50.com.criminalintent.database.CrimeDbSchema;


public class CrimeLab {
    private static CrimeLab sCrimeLab;

    List<Crime> crimes = new ArrayList<>();

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
            return sCrimeLab;
        }
        return sCrimeLab;
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getmID().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getmTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getmData().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.ismSolved() ? 1 : 0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT, crime.getmSuspect());
        values.put(CrimeDbSchema.CrimeTable.Cols.NUMBER, crime.getNumber());

        return values;
    }

    public File getFilePhoto(Crime crime) {
        File externalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFileDir == null)
            return null;

        return new File(externalFileDir, crime.getPhotoFileName());
    }

    public void addCrime(Crime crime) {
        // to insert new crime in data base
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME, null, getContentValues(crime));

    }

    public void updateCrime(Crime crime) {
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME, getContentValues(crime),
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?"
                , new String[]{crime.getmID().toString()});
    }

    public void deleteCrime(Crime crime) {
        mDatabase.delete(CrimeDbSchema.CrimeTable.NAME,
                CrimeDbSchema.CrimeTable.Cols.UUID + "= ?",
                new String[]{crime.getmID().toString()});
    }

    public List<Crime> getCrimes() {
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            crimes.clear();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();

            }

        } finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[]{id.toString()});
        if (cursor.getCount() == 0)
            return null;

        try {
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }


    }

    private int getPosition(Crime crime) {
        return crimes.indexOf(crime);
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        return new CrimeCursorWrapper(mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null, // mean return all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        ));
    }

}
