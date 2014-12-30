package by.verus.radar;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	Button btnParse;
	WebView wv;
	ProgressBar pb;
	TextView tvRadarHeader;

	String[] radarsTitle;
	String[] radarsCode;

	final String imgURL = "http://meteoinfo.by/radar/UMMN/UMMN_latest.png";
	final String radarURL = "http://meteoinfo.by/radar/?q=UMMN";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnParse = (Button) findViewById(R.id.btnParse);
		wv = (WebView) findViewById(R.id.webView1);
		pb = (ProgressBar) findViewById(R.id.pb);
		tvRadarHeader = (TextView) findViewById(R.id.tvRadarHeader);

		btnParse.setOnClickListener(this);

		// подгоняем масштаб
		wv.setInitialScale(1);
		// устанавливаем Zoom control
		wv.getSettings().setBuiltInZoomControls(true);

		pb.setVisibility(View.INVISIBLE);

		// new downloadDocTask().execute(radarURL);
	}

	@Override
	public void onClick(View v) {
		new downloadDocTask().execute(radarURL);
	}

	private class downloadDocTask extends AsyncTask<String, Void, Document> {
		@Override
		protected void onPreExecute() {
			// деактивируем кнопку которая запускает AsyncTask
			btnParse.setEnabled(false);
			pb.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Document doInBackground(String... urls) {
			Document doc = null;

			try {
				doc = Jsoup.connect(urls[0]).get();

			} catch (IOException e) {
				e.printStackTrace();
			}

			return doc;
		}

		@Override
		protected void onPostExecute(Document result) {
			super.onPostExecute(result);

			pb.setVisibility(View.INVISIBLE);
			btnParse.setEnabled(true);

			if (result != null) {
				String radarHeader = "";
				radarHeader = result.select("div#content h1").first().text();

				// Elements img = doc.select("img[src$=.png]");
				// Element img = doc.select("div#content p img").first();
				// String imgSrc = img.attr("abs:src");

				Elements radars = result.select("span#box_r a");
				radarsCode = new String[radars.size()];
				radarsTitle = new String[radars.size()];
				int i = 0;

				for (Element el : radars) {
					radarsTitle[i] = el.text();
					radarsCode[i] = el.attr("href").substring(10);

					i++;
				}

				tvRadarHeader.setText(radarHeader);
				wv.loadUrl(imgURL);
			} else {
				Toast.makeText(getApplicationContext(),
						R.string.err_download_data, Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			if (radarsTitle != null && radarsCode != null) {
				Intent intent = new Intent(this, RadarsListActivity.class);
				intent.putExtra("radarsTitle", radarsTitle);
				intent.putExtra("radarsCode", radarsCode);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						R.string.err_download_data, Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
