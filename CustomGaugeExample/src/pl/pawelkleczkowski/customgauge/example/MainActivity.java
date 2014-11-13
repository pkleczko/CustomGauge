package pl.pawelkleczkowski.customgauge.example;

import pl.pawelkleczkowski.customgauge.CustomGauge;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private CustomGauge gauge1;
	private CustomGauge gauge2;
	private CustomGauge gauge3;

	private Handler handler = new Handler();
	int i;
	private TextView text1;
	private TextView text2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button = (Button) findViewById(R.id.button);
		gauge1 = (CustomGauge) findViewById(R.id.gauge1);
		gauge2 = (CustomGauge) findViewById(R.id.gauge2);
		gauge3 = (CustomGauge) findViewById(R.id.gauge3);
		text1  = (TextView) findViewById(R.id.textView1);
		text2  = (TextView) findViewById(R.id.textView2);
		text1.setText(Integer.toString(gauge1.getValue()));
		text2.setText(Integer.toString(gauge2.getValue()));
    	text2.setText(Integer.toString(gauge2.getValue()));
    	
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
			                        	text1.setText(Integer.toString(gauge1.getValue()));
			                        	text2.setText(Integer.toString(gauge3.getValue()));
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
	
	private Runnable drawRunner = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	};
}
