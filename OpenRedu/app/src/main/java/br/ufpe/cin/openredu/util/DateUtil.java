package br.ufpe.cin.openredu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.developer.redu.models.Status;

public class DateUtil {
	private static final int SECOND = 1;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;
	private static final int DAY = 24 * HOUR;

	public static final SimpleDateFormat dfIn = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
//	public static final SimpleDateFormat dfOut = new SimpleDateFormat(
//			"dd/MM/yyyy HH:mm:ss", Locale.getDefault());

	
	public static String getFormattedStatusCreatedAt(Status status) {
		String formattedCreatAt = null;

		if (status.createdAtInMillis == 0) {
			try {
				status.createdAtInMillis = DateUtil.dfIn.parse(status.created_at).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		long today = System.currentTimeMillis();
		long deltaInSeconds = (today - status.createdAtInMillis) / 1000;

		if (deltaInSeconds < 1 * MINUTE) {
			formattedCreatAt = (deltaInSeconds == 1) ? "1 segundo atrás" : deltaInSeconds + " segundos atrás";
		}
		else if (deltaInSeconds < 2 * MINUTE) {
			formattedCreatAt = "1 minuto atrás";
		}
		else if (deltaInSeconds < 1 * HOUR) {
			formattedCreatAt = (deltaInSeconds / MINUTE) + " minutos atrás";
		}
		else if (deltaInSeconds < 2 * HOUR) {
			formattedCreatAt = "1 hora atrás";
		}
		else if (deltaInSeconds < 1 * DAY) {
			formattedCreatAt = (deltaInSeconds / HOUR) + " horas atrás";
		}
		else if (deltaInSeconds < 2 * DAY) {
			formattedCreatAt = "ontem";
		}
		else if (deltaInSeconds < 30 * DAY) {
			formattedCreatAt = (deltaInSeconds / DAY) + " dias atrás";
		}
		else {
			Calendar todayCal = Calendar.getInstance();
			todayCal.setTimeInMillis(today);

			Calendar statusCal = Calendar.getInstance();
			statusCal.setTimeInMillis(status.createdAtInMillis);
			
			int deltaInYears = todayCal.get(Calendar.YEAR) - statusCal.get(Calendar.YEAR);
			int deltaInMonths = todayCal.get(Calendar.MONTH) - statusCal.get(Calendar.MONTH) + 12 * deltaInYears;
			
			if (deltaInMonths < 12) {
				formattedCreatAt = (deltaInMonths <= 1) ? "1 mês atrás" : deltaInMonths + " meses atrás";
			} else {
				formattedCreatAt = (deltaInYears <= 1) ? "1 ano atrás" : deltaInYears + " anos atrás";
			}
		}

		
		return formattedCreatAt;
	}
}
