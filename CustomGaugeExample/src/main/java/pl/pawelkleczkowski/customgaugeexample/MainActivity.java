package pl.pawelkleczkowski.customgaugeexample;

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

		gauge1.setEndValue(10000);
		gauge2.setEndValue(700);

		text1  = findViewById(R.id.textView1);
		text2  = findViewById(R.id.textView2);
		text1.setText(Integer.toString(gauge1.getValue()));
		text2.setText(Integer.toString(gauge2.getValue()));
    	text2.setText(Integer.toString(gauge2.getValue()));

    	gauge2.setLightEffect(true);

		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread() {
			        public void run() {
			        	for (i=0;i<100;i++) {
			                try {
			                    runOnUiThread(new Runnable() {
			                        @Override
			                        public void run() {
			                        	gauge1.setValue(i*10);
			                        	gauge2.setValue(200 + i*5);
			                        	gauge3.setValue(i);
			                        	text1.setText(Integer.toString(gauge1.getEndValue()));
			                        	text2.setText(Integer.toString(gauge2.getEndValue()));
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
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
}
