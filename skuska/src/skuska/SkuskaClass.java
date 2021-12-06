package skuska;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
 
class Main
{
	
	static class programThread implements Runnable{
		private String path;
		private File nFile;
		programThread(String path, File nFile) {
			this.path = path;
			this.nFile = nFile;
		}
		public void run() {
			try {
			
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(nFile));
			ZipEntry e = new ZipEntry(path);
			out.putNextEntry(e);
			out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
    public static void downloadFile(URL url, String outputFileName) throws IOException
    {
        try (InputStream in = url.openStream();
            ReadableByteChannel rbc = Channels.newChannel(in);
            FileOutputStream fos = new FileOutputStream(outputFileName)) {
        	System.out.println("Started downloading");        	
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            System.out.println("Finished!");
        }
    }
    
    public static void main(String[] args) throws Exception{

    		//URL url = new URL("https://wwwinfo.mfcr.cz/ares/ares_vreo_all.tar.gz");
    		//String filename = "ares_vreo_all.tar.gz";
    		//downloadFile(url, filename);
    	/*try {
			 String cmd = "tar -xzvf ares_vreo_all.tar.gz 2> seznam_souboru.txt";
			 ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cmd);
               builder.redirectErrorStream(true);
               Process p = builder.start();

               BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
               StringBuffer buffer = new StringBuffer();
               String line = "";
               while (true) 
               {

                   buffer.append(line).append("\n");
                   line = r.readLine();
                   System.out.print(buffer);
                   if (line == null) { break; }
               }
               
		} catch (IOException e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	BufferedReader objReader = new BufferedReader(new FileReader("seznam_souboru.txt"));
    	String line = null;
    	String path = null;
    	while(objReader.readLine() != null) {
    		path = objReader.readLine().substring(2);
    		line = objReader.readLine().substring(16,22);
    		StringBuilder builder = new StringBuilder(line);
    		builder.insert(2, '\\');
    		builder.insert(5, '\\');
    		System.out.println(builder);
    		File nFile = new File(builder.toString());
    		System.out.println(path+nFile);
    		if(nFile.exists()) {
    			System.out.println("Existing folder.");
    			programThread thread = new programThread(path,nFile);
    			thread.run();
    			continue;
    		}else {
    			programThread thread = new programThread(path,nFile);
    			nFile.mkdirs();
    			System.out.println("Creating new folder.");
    			thread.run();
    		}
    	}
    	objReader.close();
    }
}