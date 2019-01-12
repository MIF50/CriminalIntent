package mif50.com.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

import mif50.com.criminalintent.model.Crime;

public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String UUID = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        int solved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        String suspect = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECT));
        String number = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.NUMBER));
        Crime crime = new Crime(java.util.UUID.fromString(UUID));
        crime.setmData(new Date(date));
        crime.setmTitle(title);
        crime.setmSuspect(suspect);
        crime.setNumber(number);
        crime.setmSolved(solved != 0);
        return crime;

    }
}
