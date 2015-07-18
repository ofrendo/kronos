package kronos.util;

import java.io.PrintStream;

import org.apache.log4j.Logger;

public class Log {
	protected static boolean turnedOffSLF4J = turnOffSLF4J();
	
	/* Get actual class name to be printed on */
	private static Logger log = Logger.getLogger(Log.class.getName());
   
	public static synchronized void info(String text) {
		log.info(text);
	}
    public static synchronized void warn(String text) {
	    log.warn(text);
    }
    public static synchronized void error(String text) {
 	    log.error(text);
    }
    public static synchronized void fatal(String text) {
        log.fatal(text);
    }
    
    private static boolean turnOffSLF4J() {
    	PrintStream filterOut = new PrintStream(System.err) {
    	    public void println(String l) {
    	        if (! l.startsWith("SLF4J") )
    	            super.println(l);
    	    }
    	};
    	System.setErr(filterOut);
    	return true;
    }
    
    
    
}
