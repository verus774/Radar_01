package by.verus.radar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RadarsListActivity extends Activity {

	ListView lvRadars;
	String[] testArr = { "fff", "ggg", "rrr" };

	String[] radarsTitle;
	String[] radarsCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radars_list);

		lvRadars = (ListView) findViewById(R.id.lvRadars);

		Intent intent = getIntent();
		radarsTitle = intent.getStringArrayExtra("radarsTitle");
		radarsCode = intent.getStringArrayExtra("radarsCode");

		// устанавливаем режим выбора пунктов списка
		lvRadars.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// Создаем адаптер
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, radarsTitle);
		lvRadars.setAdapter(adapter);

	}
}
