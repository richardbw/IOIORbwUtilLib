/*
    IOIO Utils.
    Copyright (C) 2012  Richard Barnes-Webb

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. 
*/

package net.babyoilbonanza.android.ioio.util;




import java.lang.reflect.Method;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
/**
 * @author rbw
 *
 */
public class Debug
{
    

    public static String tag = "Debug..>";


    public static void logGetters(Object obj) {
        dumpGetters(obj, true);
    }
    
    
    public static void dumpGetters(Object obj, boolean toLog)
    {
        if ( obj == null ) {
            out( tag +": NULL parameter; dumpGetters("+obj+")", toLog );
            return;
        }
        out( tag + ": Getters for: "+ obj.getClass(), toLog);
        
        for (Method method: obj.getClass().getMethods() ) 
            if ( method.getName().startsWith("get") && method.getParameterTypes().length == 0 ) 
                try { System.out.println(String.format("  - %-30s:%s\t(%s)", 
                            method.getName()+"()", method.invoke(obj, (new Object[]{})), method.getReturnType() ) ); } 
                catch (Exception e) { e.printStackTrace(); }
    }

/*
    public static void dumpQuery(Connection connection, String query) throws SQLException
    {
        ResultSet rs = connection.createStatement().executeQuery(query);
        ResultSetMetaData md = rs.getMetaData();
        StringBuffer sb = new StringBuffer("col> ");
        int colCount = md.getColumnCount();
        for ( int i = 1; i <= colCount; i++ ) sb.append(md.getColumnName(i) + "\t");
        System.out.println(sb);
        while ( rs.next() ) {
            sb = new StringBuffer("dat> "); //reset sb
            for ( int i = 1; i <= colCount; i++ ) sb.append(rs.getString(i) + "\t");
            System.out.println(sb);
        }
    }
*/    
    
    public static void out(String mesg, boolean toLog) {
        if ( toLog )
        {
            Log.d(mesg, mesg);
        }
        else
        {
            System.out.println( mesg );
        }
        
    }
    
    
    
    public static void bummer(Exception e, Context c) 
    {
        dialog(e.getClass().getSimpleName(), getExceptionMesg(e), c);
        Log.e("Alert", "Exception!?:\n"+e.getMessage(), e);
        e.printStackTrace();
    }



    public static String getExceptionMesg(Exception e)
    {
        String mesg = (e.getMessage() != null) ? e.getMessage() : "";

        String packagePref = Debug.class.getPackage().getName().substring(0, Debug.class.getPackage().getName().indexOf('.'));
        for ( StackTraceElement element : e.getStackTrace() ) {
            if ( element.toString().startsWith(packagePref) ) {
                mesg += "\n  At:\n" + element;
                break;
            }
        }
        return mesg;
    }
    
    
    public static void dialog(String t, String s, Context c) {
        (new AlertDialog.Builder(c))
            .setTitle(t)
            .setMessage(s)
            .setIcon(R.drawable.wile_e_coyote_error)
            .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                   }
               })
            .create()
        .show();
    }

    

    //--------------------------
    public static int DEBUG_LEVEL;
    public static DebugOutput debugOut;
    
    public static void logTrace(String str) {
        if (DEBUG_LEVEL >= 5 ) {
            appendLogData("T:"+str); 
        }
    }
    public static void logDebug(String str) {
        if (DEBUG_LEVEL >= 4 ) {
            appendLogData("D:"+str); 
        }
    }
    public static void logInfo(String str) {
        if (DEBUG_LEVEL >= 3 ) {
            appendLogData("I:"+str); 
        }
    }
    public static void logError(String str) {
        appendLogData("E:"+str); 
    }
    public static void logException(Exception e) {
        appendLogData("Ex:"+getExceptionMesg(e)); 
        Log.e("Util:", e.getMessage());
    }

    public static void appendLogData(String s) {
        if (debugOut != null) {
            debugOut.appendLogTxt(tag+": "+s);
        } 
    }
    //--------------------------
    


}
