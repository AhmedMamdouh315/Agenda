package mamdouh.ahmed.agenda;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Utils
{
    static String book_title,author_name,image_url,publisher,description,date;
    public  static ArrayList<DataClass>FetchBooksData(String url)  {
        URL url1= null;
        try {
            url1 = createURL(url);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }


        String JsonResponse=null;


        try
        {
            JsonResponse =MakeHttpRequest(url1);
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }


        ArrayList<DataClass> booksarraylist = null;
        try {
            booksarraylist = extractFromjson(JsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return booksarraylist  ;

    }

    public static URL createURL (String stringurl) throws MalformedURLException
    {
        URL url = null;

            url = new URL(stringurl);

    return  url;
    }

    public static  String MakeHttpRequest (URL url) throws IOException
    {
        String jsonrespons ="";
        if (url == null)
        {
            return jsonrespons;
        }

    HttpURLConnection urlConnection =null;
        InputStream inputStream= null;
            urlConnection =(HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");

            urlConnection.connect();
            if(urlConnection.getResponseCode()==200)
            {
                inputStream =urlConnection.getInputStream();
                jsonrespons=readfromstream(inputStream);
            }
            if (urlConnection!=null)
            {
                urlConnection.disconnect();
            }
            if(inputStream!=null)
            {
                inputStream.close();
            }
            return jsonrespons;



    }

    public static  String readfromstream (InputStream inputStream) throws IOException
    {
        StringBuilder stringBuilder =new StringBuilder();

        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream
                    ,Charset.defaultCharset());
            BufferedReader bufferedReader =new BufferedReader(inputStreamReader);

            String line=bufferedReader.readLine();

            while (line != null)
            {
                stringBuilder.append(line);
                line=bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    public static ArrayList<DataClass>extractFromjson (String jsonresponse) throws IOException ,JSONException
    {
        if(TextUtils.isEmpty(jsonresponse)&&jsonresponse==null)
        {
            return null;
        }
        ArrayList<DataClass>booksdata=new ArrayList<>();
            JSONObject baseobject =new JSONObject(jsonresponse);
            JSONArray itemsarray =baseobject.getJSONArray("items");

            for(int i=0 ; i<itemsarray.length();i++)
            {
                JSONObject item= itemsarray.getJSONObject(i);
                JSONObject info =item.getJSONObject("volumeInfo");
                if(info.has("title")) {
                    book_title = info.getString("title");
                }
                else { book_title = "Title Not Found";}

                if (info.has("authors"))
                {
                    author_name = info.getJSONArray("authors").getString(0);
                }
                else
                {
                    author_name="Book Author Not Found";
                }
                if (info.has("publisher"))
                {
                    publisher=info.getString("publisher");
                }
                else
                {
                    publisher = "Publisher Not Found";
                }


                if (info.has("publishedDate"))
                {
                    date=info.getString("publishedDate");
                }
                else
                {
                    date = "Publish Date Not Found";
                }


                if (info.has("description"))
                {
                    description=info.getString("description");
                }
                else
                {
                    description = "Description  Not Found";
                }

                if (info.has("imageLinks"))
                {
                    image_url=info.getJSONObject("imageLinks").getString("thumbnail");
                }
                else
                {
                    image_url = "";
                }

                booksdata.add(new DataClass(book_title,author_name,image_url,publisher,description,date));
            }
            return booksdata;



    }

}
