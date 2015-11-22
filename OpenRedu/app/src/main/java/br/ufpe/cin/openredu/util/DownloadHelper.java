package br.ufpe.cin.openredu.util;




import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

public class DownloadHelper {
	
	public static final String PATH_SUPPORT_MATERIAL_DOWNLOAD = "/redu_mobile/material_apoio";
	public static final String PATH_LECTURE_DOWNLOAD = "/redu_mobile/lecture";
	
	// 
	/* ** Metodo que abre o arquivos, independentemente dos tipos **
	 * Tipos de Arquivos suportados:
	'application/x-mp4',
    'video/x-flv',
    'application/x-flv',
    'video/mpeg',
    'video/quicktime',
    'video/x-la-asf',
    'video/x-ms-asf',
    'video/x-msvideo',
    'video/x-sgi-movie',
    'video/x-flv',
    'flv-application/octet-stream',
    'video/3gpp',
    'video/3gpp2',
    'video/3gpp-tt',
    'video/BMPEG',
    'video/BT656',
    'video/CelB',
    'video/DV',
    'video/H261',
    'video/H263',
    'video/H263-1998',
    'video/H263-2000',
    'video/H264',
    'video/JPEG',
    'video/MJ2',
    'video/MP1S',
    'video/MP2P',
    'video/MP2T',
    'video/mp4',
    'video/MP4V-ES',
    'video/MPV',
    'video/mpeg4',
    'video/mpeg',
    'video/avi',
    'video/mpeg4-generic',
    'video/nv',
    'video/vnd.objectvideo',
    'video/parityfec',
    'video/pointer',
    'video/raw',
    'video/rtx'
	*/
	public static Intent loadDocInReader(File file) throws ActivityNotFoundException, Exception {
		Uri path = Uri.fromFile(file);
		Intent it = new Intent(Intent.ACTION_VIEW);
		it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if(file.getName().endsWith(".pdf") || file.getName().endsWith(".PDF"))
			it.setDataAndType(path, "application/pdf");
		else if (file.getName().endsWith(".WAV") || file.getName().endsWith(".wav"))
			it.setDataAndType(path, "audio/vnd.wave");
		else if(file.getName().endsWith(".MP3") || file.getName().endsWith(".mp3"))
			it.setDataAndType(path, "audio/mpeg");
		else if(file.getName().endsWith(".DOC") || file.getName().endsWith(".doc"))
			it.setDataAndType(path, "application/msword");
		else if(file.getName().endsWith(".DOCX") || file.getName().endsWith(".docx"))
			it.setDataAndType(path, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		else if(file.getName().endsWith(".PPT") || file.getName().endsWith(".ppt"))
			it.setDataAndType(path, "application/vnd.ms-powerpoint");
		else if(file.getName().endsWith(".PPTX") || file.getName().endsWith(".pptx"))
			it.setDataAndType(path, "application/vnd.openxmlformats-officedocument.presentationml.presentation");
		else if(file.getName().endsWith(".PPSX") || file.getName().endsWith(".ppsx"))
			it.setDataAndType(path, "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
		else if(file.getName().endsWith(".JPG") || file.getName().endsWith(".jpg") || 
				file.getName().endsWith(".JPEG") || file.getName().endsWith(".jpeg"))
			it.setDataAndType(path, "image/jpeg");
		else if(file.getName().endsWith(".PNG") || file.getName().endsWith(".png"))
			it.setDataAndType(path, "image/png");
		else if (file.getName().endsWith(".TXT") || file.getName().endsWith(".txt"))
			it.setDataAndType(path, "text/plain");
		else if (file.getName().endsWith(".MP4") || file.getName().endsWith(".mp4"))
			it.setDataAndType(path, "video/mp4");
		else
			it.setDataAndType(path, "text/plain");
		return it;
	}
	
	public static Intent loadDocInReader(File file, String mimeType) throws ActivityNotFoundException, Exception {
		Uri path = Uri.fromFile(file);
		Intent it = new Intent(Intent.ACTION_VIEW);
		it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		it.setDataAndType(path, mimeType);
		return it;
	}
	
	public static String getSupportMaterialPath(){
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		return extStorageDirectory+DownloadHelper.PATH_SUPPORT_MATERIAL_DOWNLOAD;
	}
	
	public static String getLecturePath(){
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		return extStorageDirectory+DownloadHelper.PATH_LECTURE_DOWNLOAD;
	}
}
