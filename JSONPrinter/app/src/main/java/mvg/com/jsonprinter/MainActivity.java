package mvg.com.jsonprinter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

	LineAdapter adapter;

	public class DoAsyncGet extends AsyncTask<HttpGet, Void, JSONObject> {
		@Override
		protected JSONObject doInBackground(HttpGet... params) {
			if(params[0] != null) {
				// Create a client to send the request
				HttpClient client = new DefaultHttpClient();
				try {
					HttpResponse response = client.execute(params[0]);

					// Read in all the lines from the response into a single line for the JSON parser
					BufferedReader bfRead = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					String json = "";
					String line = "";
					while(line != null) {
						line = bfRead.readLine();
						if(line != null) {
							json += line;
						}
					}

					Log.d("Step 1:", json);

					// Parse the JSON and go back to the UI thread
					return new JSONObject(json);
				} catch (IOException e) {
					Log.d("grabJson/doAsyncGet", "Connection issue");
				} catch (JSONException e) {
					Log.d("grabJson/doAsyncGet", "JSON parsing issue");
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(JSONObject jsonReply) {
			if(jsonReply == null){
				Log.d("grabJson/postExecute", "There was an issue getting the JSON");
				return;
			}

			try {

				// get the JSON Array with the event data into an object, then get all of the images
				// for each event in another thread
				JSONArray guideArray = jsonReply.getJSONArray("data");
				DoAsyncGetImages doAsyncGetImages = new DoAsyncGetImages();
				doAsyncGetImages.execute(guideArray);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public class DoAsyncGetImages extends AsyncTask<JSONArray, Void, Bitmap[]>{
		private JSONArray input;

		@Override
		protected Bitmap[] doInBackground(JSONArray... params) {
			JSONArray jsonArr = params[0];
			input = jsonArr;
			Bitmap[] images = new Bitmap[jsonArr.length()];

			for(int i = 0; i < jsonArr.length(); i++){
				// create a URL for each bitmap from the "icon" field
				URL url;
				try {
					url = new URL((String)(((JSONObject)jsonArr.get(i)).get("icon")));
				} catch (MalformedURLException e) {
					Log.d("doAsyncGetImages", "Bad URL provided");
					continue;
				} catch (JSONException e) {
					Log.d("doAsyncGetImages", "JSON error");
					continue;
				}

				// pull the bitmap from the URL and read it in
				try {
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					images[i] = BitmapFactory.decodeStream(input);
				} catch (IOException e) {
					Log.d("doAsyncGetImages", "Connection error");
					continue;
				}
			}
			return images;
		}

		@Override
		protected void onPostExecute(Bitmap[] images){
			// give the adapter what it needs to generate each listing
			adapter.init(input, images);
			ListView listView = (ListView)findViewById(R.id.lv_jsonObjects);
			listView.setAdapter(adapter);
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new LineAdapter(this);
		setContentView(R.layout.activity_main);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	public void grabJSON(View v) {

		// URL to grab JSON from
		String url = "http://guidebook.com/service/v2/upcomingGuides/";
		try {

			// Form a GET request and run it in a different thread
			HttpGet getRequest = new HttpGet(new URI(url));
			DoAsyncGet asyncGet = new DoAsyncGet();
			asyncGet.execute(getRequest);
		} catch (URISyntaxException e) {
			Log.d("grabJson", "URI could not be parsed");
		}
	}
}
