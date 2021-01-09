package ieeeWrapper.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class HttpService {
    public static String executeGet(final String httpUrl) {
        String response = "";

        try {
            URL url = new URL(httpUrl);

            URLConnection urlConnection = url.openConnection();

            InputStream inputStream = urlConnection.getInputStream();

            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            response = s.hasNext() ? s.next() : "";

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
