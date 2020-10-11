import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebWorker extends Thread {

    private String urlString;
    private WebView frame;
    private int row;

    public WebWorker(String urlString, int row, WebView frame) {
        this.frame = frame;
        this.urlString = urlString;
        this.row = row;
    }

    @Override
    public void run() {
        frame.changeWorkerCount(1);
        frame.updateRow(row, "...fetching...");
        downloadAll();
        frame.sendCompletionNotice();
    }

    private void downloadSuccess(int size, long elapsedTime) {
        StringBuffer buff = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        format.format(new Date(System.currentTimeMillis()), buff, new FieldPosition(0));
        buff.append(" ").append(elapsedTime).append("ms ").append(size).append(" bytes");
        frame.updateRow(row, new String(buff));
    }

    private void downloadAll() {
        InputStream input = null;
        StringBuilder contents;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            long startTime = System.currentTimeMillis();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            input = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                contents.append(array, 0, len);
                Thread.sleep(100);
            }

            // Successful download if we get here
            downloadSuccess(contents.length(), System.currentTimeMillis() - startTime);
        }
        // Otherwise control jumps to a catch...
        catch (InterruptedException exception) {
            frame.updateRow(row, "interrupted");
        } catch (IOException exception) {
            frame.updateRow(row, "err");
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ignored) {
            }
        }


    }

}
