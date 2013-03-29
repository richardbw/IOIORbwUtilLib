/**
 * 
 */
package net.babyoilbonanza.android.ioio.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * @author rbw
 *
 * @see: http://www.androidsnippets.com/how-to-capture-application-log
 */
public class TailLogThread extends Thread
{
    
    private int MAX_LOG_LINES = 10;
    private String _logcatTxt;
    private Handler _handler;
    private int _handler_what;

    public TailLogThread(Handler handler, int what, String logcatTxt, int no_log_lines)
    {
       _logcatTxt = logcatTxt;
       _handler = handler;
       _handler_what = what;
       MAX_LOG_LINES = no_log_lines;
    }
    
    @Override
    public void run()
    {
        Log.d(Debug.tag, "Starting to tail log..");

        Process mLogcatProc = null;
        BufferedReader reader = null;
        try
        {
            //DELMEmLogcatProc = Runtime.getRuntime().exec(new String[] { "logcat", "-d", "AndroidRuntime:E "+_logcatTxt+" *:S" });
            mLogcatProc = Runtime.getRuntime().exec(new String[] { "logcat", "-d", _logcatTxt });

            reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));

            final StringBuilder log = new StringBuilder();

            ArrayList<String> logList = new ArrayList<String>();
            
            String line;
            while ( (line = reader.readLine()) != null )
            {
                logList.add(line);
            }

            //limit to last MAX_LOG_LINES:
            for ( int i = (logList.size()>MAX_LOG_LINES?logList.size()-MAX_LOG_LINES:0);  i < logList.size(); i++ )
                log.append(logList.get(i)+System.getProperty("line.separator"));
            
            _handler.sendMessage(Message.obtain(_handler, _handler_what, log));
            
        }

        catch (IOException e)
        {
            Log.e(Debug.tag, "---> TailLogThread Exception: "+e.getMessage(), e);
        }

        finally
        {
            if ( reader != null ) try
            {
                reader.close();
            }
            catch (IOException e)
            {
                Log.e(Debug.tag, "---> TailLogThread Exception: "+e.getMessage(), e);
            }

        }


    }

}
