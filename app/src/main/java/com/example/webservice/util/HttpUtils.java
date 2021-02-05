package com.example.webservice.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * utility class for perform Http related functions
 */
public class HttpUtils {

    /**
     * perform an Http POST to the supplied urlString with the supplied
     * requestHeaders and formParameters
     * @return String the response contents
     * @param urlString the URL to post to
     * @param requestHeaders a Map of the request headernames and values to
     * be placed into the request
     * @param formParameters a Map of form parameters and values to be placed
     * into the request
     * @throws MalformedURLException reports problems with the urlString
     * @throws ProtocolException reports problems performing Http POST
     * @throws IOException reports I/O sending and/or retrieving data over Http
     */
    public static String postForm(String urlString,
                                  Map requestHeaders,
                                  Map formParameters )
            throws MalformedURLException, ProtocolException, IOException {
        return post( urlString, requestHeaders, formParameters, null );
    }

    /**
     * perform an Http POST to the supplied urlString with the supplied
     * requestHeaders and contents
     * @return String the response contents
     * @param urlString the URL to post to
     * @param requestHeaders a Map of the request headernames and values to
     * be placed into the request
     * @param contents the contents of the HTTP request
     * @throws MalformedURLException reports problems with the urlString
     * @throws ProtocolException reports problems performing Http POST
     * @throws IOException reports I/O sending and/or retrieving data over Http
     */
    public static String postContents(String urlString,
                                      Map requestHeaders,
                                      String contents )
            throws MalformedURLException, ProtocolException, IOException {
        return post( urlString, requestHeaders, null, contents );
    }

    /**
     * perform an Http POST to the supplied urlString with the supplied
     * requestHeaders and formParameters
     * @return String the response contents
     * @param urlString the URL to post to
     * @param requestHeaders a Map of the request headernames and values to
     * be placed into the request
     * @param formParameters a Map of form parameters and values to be placed
     * into the request
     * @param
     * @throws MalformedURLException reports problems with the urlString
     * @throws ProtocolException reports problems performing Http POST
     * @throws IOException reports I/O sending and/or retrieving data over Http
     */
    public static String post(String urlString,
                              Map requestHeaders,
                              Map formParameters,
                              String requestContents )
            throws MalformedURLException, ProtocolException, IOException {
        // open url connection
        URL url = new URL( urlString );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // set up url connection to post information and
        // retrieve information back
        con.setRequestMethod( "POST" );
        con.setDoInput( true );
        con.setDoOutput( true );
        con.setUseCaches(false);

        // add all the request headers
        if( requestHeaders != null ) {
            Set headers = requestHeaders.keySet();
            for(Iterator it = headers.iterator(); it.hasNext(); ) {
                String headerName = (String) it.next();
                String headerValue = (String) requestHeaders.get( headerName );
                con.setRequestProperty( headerName, headerValue );
            }
        }

        // add url form parameters
        DataOutputStream ostream = null;

        StringBuffer buffer = new StringBuffer();

        try {
            ostream = new DataOutputStream( con.getOutputStream() );
            if( formParameters != null ) {
                Set parameters = formParameters.keySet();
                Iterator it = parameters.iterator();
                StringBuffer buf = new StringBuffer();

                for( int i = 0, paramCount = 0; it.hasNext(); i++ ) {
                    String parameterName = (String) it.next();
                    String parameterValue = (String) formParameters.get( parameterName );

                    if( parameterValue != null ) {
                        parameterValue = URLEncoder.encode(parameterValue);
                        if( paramCount > 0 ) {
                            buf.append( "&" );
                        }
                        buf.append(parameterName);
                        buf.append("=");
                        buf.append(parameterValue);
                        ++paramCount;
                    }
                }
                //System.out.println( "adding post parameters: " + buf.toString() );
                ostream.writeBytes( buf.toString() );
            }

            if( requestContents != null ) {
                ostream.write(requestContents.getBytes("UTF-8"));
            }

            Object contents = con.getContent();
            InputStream is = (InputStream) contents;

            int c;
            while( ( c = is.read() ) != -1 ) {
                buffer.append( (char) c );
            }

            if (con.getResponseCode() != 200){
                con.disconnect();
                return null;
            }

            con.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if( ostream != null ) {
                ostream.flush();
                ostream.close();
            }
        }


        return buffer.toString();
    }

    public static String postMultipartFormData(String url, Map formParameters, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        HttpURLConnection con = null;
        StringBuffer buffer = null;
        OutputStream os = null;

            con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Connection", "Keep-Alive");
            String boundary =  "SwA"+ Long.toString(System.currentTimeMillis())+"SwA";
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.connect();
            os = con.getOutputStream();

        try{
            // Add form parameters
            if( formParameters != null ) {
                Set parameters = formParameters.keySet();
                Iterator it = parameters.iterator();
                StringBuffer buf = new StringBuffer();

                for( int i = 0; it.hasNext(); i++ ) {
                    String parameterName = (String) it.next();
                    String parameterValue = (String) formParameters.get( parameterName );

                    if( parameterValue != null ) {
                        parameterValue = URLEncoder.encode(parameterValue);

                        os.write( ("--" + boundary + "\r\n").getBytes());
                        os.write( "Content-Type: text/plain\r\n".getBytes());
                        os.write( ("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n").getBytes());;
                        os.write( ("\r\n" + parameterValue + "\r\n").getBytes());

                    }
                }
            }

            // Adiciona imagem no upload
            if (byteArrayOutputStream != null){
                os.write( ("--" + boundary + "\r\n").getBytes());
                os.write(("Content-Disposition: form-data; name=\"" + "file" + "\"; filename=\"" + "image.png" + "\"\r\n").getBytes());
                os.write( ("Content-Type: application/octet-stream\r\n"  ).getBytes());
                os.write( ("Content-Transfer-Encoding: binary\r\n"  ).getBytes());
                os.write("\r\n".getBytes());

                os.write(byteArrayOutputStream.toByteArray());

                os.write("\r\n".getBytes());
            }

            os.write( ("--" + boundary + "--" + "\r\n").getBytes());

            InputStream is = con.getInputStream();
            byte[] b1 = new byte[1024];
            buffer = new StringBuffer();

            while ( is.read(b1) != -1)
                buffer.append(new String(b1));

            if (con.getResponseCode() != 200){
                con.disconnect();
                return null;
            }

            con.disconnect();

            return buffer.toString();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if( os != null ) {
                os.flush();
                os.close();
            }
        }

        return null;

    }

    public static String postMultipartFormDataMedia(String url, String fileName, Map formParameters, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        HttpURLConnection con = null;
        StringBuffer buffer = null;
        OutputStream os = null;

        con = (HttpURLConnection) ( new URL(url)).openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestProperty("Connection", "Keep-Alive");
        String boundary =  "SwA"+ Long.toString(System.currentTimeMillis())+"SwA";
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.connect();
        os = con.getOutputStream();

        try{
            // Add form parameters
            if( formParameters != null ) {
                Set parameters = formParameters.keySet();
                Iterator it = parameters.iterator();
                StringBuffer buf = new StringBuffer();

                for( int i = 0; it.hasNext(); i++ ) {
                    String parameterName = (String) it.next();
                    String parameterValue = (String) formParameters.get( parameterName );

                    if( parameterValue != null ) {
                        parameterValue = URLEncoder.encode(parameterValue);

                        os.write( ("--" + boundary + "\r\n").getBytes());
                        os.write( "Content-Type: text/plain\r\n".getBytes());
                        os.write( ("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n").getBytes());;
                        os.write( ("\r\n" + parameterValue + "\r\n").getBytes());

                    }
                }
            }

            // Adiciona imagem no upload
            if (byteArrayOutputStream != null){
                os.write( ("--" + boundary + "\r\n").getBytes());
                os.write(("Content-Disposition: form-data; name=\"" + "file" + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
                os.write( ("Content-Type: application/octet-stream\r\n"  ).getBytes());
                os.write( ("Content-Transfer-Encoding: binary\r\n"  ).getBytes());
                os.write("\r\n".getBytes());

                os.write(byteArrayOutputStream.toByteArray());

                os.write("\r\n".getBytes());
            }

            os.write( ("--" + boundary + "--" + "\r\n").getBytes());

            InputStream is = con.getInputStream();
            byte[] b1 = new byte[1024];
            buffer = new StringBuffer();

            while ( is.read(b1) != -1)
                buffer.append(new String(b1));

            if (con.getResponseCode() != 200){
                con.disconnect();
                return null;
            }

            con.disconnect();

            return buffer.toString();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if( os != null ) {
                os.flush();
                os.close();
            }
        }

        return null;

    }

    public static String get(Map requestHeaders, String path) {

        try {
            URL url = new URL(path);
            URLConnection conn = url.openConnection();

            HttpURLConnection con = (HttpURLConnection) conn;
            con.setAllowUserInteraction(false);
            con.setInstanceFollowRedirects(true);
            con.setRequestMethod("GET");
            con.setUseCaches(false);

            // add all the request headers
            if( requestHeaders != null ) {
                Set headers = requestHeaders.keySet();
                for(Iterator it = headers.iterator(); it.hasNext(); ) {
                    String headerName = (String) it.next();
                    String headerValue = (String) requestHeaders.get( headerName );
                    con.setRequestProperty( headerName, headerValue );
                }
            }
            con.connect();

            InputStream is = con.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (is != null) {

                String line;

                try {
                    BufferedReader r1 = new BufferedReader(new InputStreamReader(
                            is, "UTF-8"));
                    while ((line = r1.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }
                } finally {
                    is.close();
                }
            }


            if (con.getResponseCode() != 200){
                con.disconnect();
                return null;
            }

            con.disconnect();

            return buffer.toString();

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
