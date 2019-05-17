package pl.pawelkleczkowski.customgaugeexample;

import java.util.Locale;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import pl.pawelkleczkowski.customgauge.CustomGauge;


public class MainActivity extends AppCompatActivity {

	private CustomGauge gauge1;
	private CustomGauge gauge2;
	private CustomGauge gauge3;

	int i;
	private TextView text1;
	private TextView text2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(getString(R.string.app_name));
		setSupportActionBar(toolbar);

		Button button = findViewById(R.id.button);
		gauge1 = findViewById(R.id.gauge1);
		gauge2 = findViewById(R.id.gauge2);
		gauge3 = findViewById(R.id.gauge3);

		text1  = findViewById(R.id.textView1);
		text2  = findViewById(R.id.textView2);
		text1.setText(String.valueOf(gauge1.getValue()));
		text2.setText(String.valueOf(gauge2.getValue()));
    	text2.setText(String.valueOf(gauge2.getValue()));
    	
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gauge2.setEndValue(800);
				gauge2.setValue(200);
				text2.setText(String.format(Locale.getDefault(), "%1d/%2d", gauge2.getValue(), gauge2.getEndValue()));
				new Thread() {
			        public void run() {
			        	for (i=0;i<100;i++) {
					        if (i == 50) {
						        gauge2.setEndValue(1200);
					        }
			                try {
			                    runOnUiThread(new Runnable() {
			                        @Override
			                        public void run() {
			                        	gauge1.setValue(i*10);
			                        	gauge2.setValue(200 + i*5);
			                        	gauge3.setValue(i);
			                        	text1.setText(String.valueOf(gauge1.getValue()));
			                        	text2.setText(String.format(Locale.getDefault(), "%1d/%2d", gauge2.getValue(), gauge2.getEndValue()));
			                        }
			                    });
			                    Thread.sleep(50);
			                } catch (InterruptedException e) {
			                    e.printStackTrace();
			                }
			            }
			        }
			    }.start();
			}
		});
	}
	
}
