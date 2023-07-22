package flcws.windows.simulator;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.media.*;
import android.content.res.*;
import java.io.*;
import android.net.*;
import android.hardware.*;
import android.util.*;
import android.widget.Toast.*;


public class MainActivity extends Activity implements SurfaceHolder.Callback
{
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
	private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        surfaceView = findViewById(R.id.sfv);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    // 实现 SurfaceHolder.Callback 接口的回调方法
    @Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		final SurfaceHolder finalHolder = holder;
		runOnUiThread(new Runnable() {
				@Override
				public void run()
				{
					drawOnSurface(finalHolder);
				}
			});
	}

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
        // 在 Surface 尺寸变化时执行相应操作
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
	{
        // 在 Surface 销毁时执行相应操作
    }

    private void drawOnSurface(SurfaceHolder holder)
	{
        Canvas canvas = holder.lockCanvas();
        if (canvas != null)
		{
            // 开启抗锯齿
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setAntiAlias(true);
			//背景
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
			//启动文字
            paint.setColor(Color.WHITE);
            paint.setTextSize(30f);
			paint.setTextAlign(Paint.Align.CENTER);

			float x = canvas.getWidth() / 2f;
			float y = canvas.getHeight() - 50f;

            canvas.drawText("Starting Windows", x, y, paint);
            holder.unlockCanvasAndPost(canvas);
			new Thread(new Runnable(){
					@Override
					public void run()
					{
						try
						{
							//等待5秒
							Thread.sleep(5000);
							//启动
							boot();
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
							Looper.loop();
						}
					}
				}).start();
        }
    }
	public static Bitmap bitMapScale(Bitmap bitmap, float scale)
	{
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }
	private void boot()
	{            
		surfaceView = findViewById(R.id.sfv);
		SurfaceHolder holder = surfaceView.getHolder();

		Canvas canvas = holder.lockCanvas();
        if (canvas != null)
		{
            // 开启抗锯齿
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setAntiAlias(true);
			//背景
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
            holder.unlockCanvasAndPost(canvas);
			try
			{
				AssetManager assetManager = getAssets();
				AssetFileDescriptor afd = assetManager.openFd("startup-win7.mp3");
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				mediaPlayer.prepare();
				mediaPlayer.start();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Looper.loop();
			}
			try
			{
				DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
				int height = displayMetrics.heightPixels;
				int width = displayMetrics.widthPixels;
				Bitmap bitmap =   BitmapFactory.decodeResource(getResources(), R.drawable.bg_desktop);
				bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
				canvas = holder.lockCanvas();  // 先锁定当前surfaceView的画布
				canvas.drawBitmap(bitmap, 0, 0, paint); //执行绘制操作
				holder.unlockCanvasAndPost(canvas); // 解除锁定并显示在界面上
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Looper.loop();
			}
		}
	}
	private void BSOD(String errinfo)
	{
		surfaceView = findViewById(R.id.sfv);
		SurfaceHolder holder = surfaceView.getHolder();
		Canvas canvas = holder.lockCanvas();
		// 开启抗锯齿
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);
		//背景
		paint.setColor(Color.parseColor("#0079D8"));
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
		paint.setTextSize(50f);
		canvas.drawText(":(",20,20,paint);
		canvas.drawText("Error Code: "+errinfo+"\nAbout the problem's more info, please visit https://flcws-f.github.io/WinSimemu/About_BSOD.html\nIf you want to send email to admin, please give the stop code. ",20,100,paint);
		holder.unlockCanvasAndPost(canvas);
	}
}
