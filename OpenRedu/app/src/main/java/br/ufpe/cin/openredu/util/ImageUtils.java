package br.ufpe.cin.openredu.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class ImageUtils {

	public static Bitmap getCroppedBitmap(Bitmap bitmap, int croppedWidth, int croppedHeight) throws Exception {

		int bmpW = bitmap.getWidth();
		int bmpH = bitmap.getHeight();

		int bmpX = 0;
		int bmpY = 0;

		//Normalize
		if (bmpW > croppedWidth) {
			bmpX += (bmpW - croppedWidth) / 2;
			bmpW = croppedWidth;
		}

		if (bmpH > croppedHeight) {
			bmpY += (bmpH - croppedHeight) / 2;
			bmpH = croppedHeight;
		}

		return Bitmap.createBitmap(bitmap, bmpX, bmpY, bmpW, bmpH);
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = roundPixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static float dipToPx(int dip, Resources res) {

		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, res.getDisplayMetrics());
	}

	/**
	 * Redimensiona um bitmap proporcionalmente ao tamanho da tela, sem deixar gaps, como acontece com as opcoes de scale padrões do Android.
	 * @param context Contexto onde o bitmap sera inserido.
	 * @param bmp Bitmap original.
	 * @return Bitmap resized.
	 */
	public static Bitmap scaleBitmapToScreen(Context context, Bitmap bmp) {

		if(bmp == null) {
			return null;
		}

		int imgWidth = bmp.getWidth();
		int imgHeight = bmp.getHeight();

		Point screenDimensions = getScreenDimensions(context);
		double width = screenDimensions.x;
		double height = screenDimensions.y;

		double scale;
		if(imgWidth > width && imgHeight > height) {
			if(imgWidth > imgHeight) {
				scale = width / imgWidth;
			}
			else {
				scale = height / imgHeight;
			}
		}
		else if(imgWidth > width) {
			scale = width / imgWidth;
		}
		else if(imgHeight > height) {
			scale = height / imgHeight;
		}
		else {
			scale = 1.0;
		}

		int newImgWidth = (int)(imgWidth * scale);
		int newImgHeight = (int)(imgHeight * scale);

		return Bitmap.createScaledBitmap(bmp, newImgWidth, newImgHeight, false);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static Point getScreenDimensions(Context context) {

		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		Point point = new Point();

		if (android.os.Build.VERSION.SDK_INT >= 13) {
			display.getSize(point);
		} else {
			point.x = display.getWidth();
			point.y = display.getHeight();
		}

		return point;
	}
}
