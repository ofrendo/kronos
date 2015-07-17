package kronos.sim;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Observable;

import kronos.util.Log;

public class SAReader extends Observable implements Runnable, SimSource {
	
	private String dirPath = "sim/data/";
	private Path dir;
	
	public SAReader() {
		dir = Paths.get(dirPath);
		cleanDataDirectory();
		Log.info("SAReader: Constructor, cleaned old files.");
	}
	
	/**
	 * Clean all old .erp files from data directory
	 */
	public void cleanDataDirectory() {
		File file = new File(dirPath);
		File[] files = file.listFiles(); 
		for (File f:files) {
			if (f.isFile() && f.getName().endsWith(".erp")) { 
				f.delete();
			}
		}
	}
	
	public void run() {
		while (ConnectionHandler.getKeepListening() == true) {
			
			try {
				Log.info("SAReader: Starting to listen " + dir.toAbsolutePath());
				WatchService watcher = dir.getFileSystem().newWatchService();
				
				dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
			    
			    // Wait for key to be signaled
			    WatchKey key = watcher.take();
			    onFileEvent(key);
			    
			    // Reset the key for future events
			    boolean valid = key.reset();
			    if (!valid) {
			        break;
			    }
			    
			} catch (Exception e) {
				Log.error("SAReader run() error: " + e.getMessage());
			}
		}
		
	}
	
	private void onFileEvent(WatchKey key) {
		for (WatchEvent<?> event: key.pollEvents()) {
	        WatchEvent.Kind<?> kind = event.kind();
	        
	        // Only look for create events
	        if (kind == OVERFLOW) {
	            continue;
	        }

	        // The filename is the context of the event.
	        @SuppressWarnings("unchecked")
			WatchEvent<Path> ev = (WatchEvent<Path>)event;
	        Path fileName = ev.context();
	        if (!fileName.toString().endsWith(".erp")) {
	        	continue;
	        }
	        Path completePath = dir.resolve(fileName);
	        String path = completePath.toAbsolutePath().toString();
	        Log.info("SAReader: Reading file " + path + "...");
	        
			File file = new File(path);
			onSAFile(file);

	    }
	}
	
	public void onSAFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();

			String result = new String(data, "UTF-8");
			Log.info("SAReader: File length: " + file.length() + ", result: " + result);
			this.setChanged();
			this.notifyObservers(result);
		}
		catch (Exception e) {
			Log.error("SAReader onSAFile() error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		(new SAReader()).run();
	}

	public SimSourceType getType() {
		return SimSourceType.saData;
	}
}
