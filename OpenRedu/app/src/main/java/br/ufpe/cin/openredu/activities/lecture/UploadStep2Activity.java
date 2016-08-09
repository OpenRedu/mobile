package br.ufpe.cin.openredu.activities.lecture;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import br.com.developer.redu.models.Space;
import br.com.developer.redu.models.Subject;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.adapters.PopupAdapter;

public class UploadStep2Activity extends Activity {

	String superId;
	Space space;
	String type;
	private String filemanagerstring;
	private String selectedImagePath;
	private Subject mSubject;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_file_or_lecture);
		TextView tvTitle = (TextView)findViewById(R.id.tvTitleUpload2);
		tvTitle.setText("Escolha a forma de adição?");
		superId = getIntent().getExtras().getString("id");
		type = getIntent().getExtras().getString("type");
		space = (Space)getIntent().getExtras().get(Space.class.getName());
		mSubject = (Subject)getIntent().getExtras().get(Subject.class.getName());
		ListView lv = (ListView)findViewById(R.id.lvInsertFileFolder);
		String[] str = {"Camera","Escolher da Galeria"};
		if (mSubject != null) {
			lv.setAdapter(new PopupAdapter(this, str, space, mSubject));
		}else{
			lv.setAdapter(new PopupAdapter(this, str, superId, space));
		}
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// If it's a recording action
				if (position == 0) {
					if (type.equals("foto")) {
						Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(cameraIntent, 2);
					}else if(type.equals("video")){
						Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
						startActivityForResult(cameraIntent, 2);
					}else{
						
					}
					
				}
				// If it's a selection action
				if (position == 1) {
					if (type.equals("foto")) {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent, "Escolha a Imagem"), 2);
					}
					if (type.equals("video")) {
						Intent intent = new Intent();
						intent.setType("video/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent, "Escolha o video"), 2);
					}
					if (type.equals("audio")) {
						
					}
				}
			}
		});
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
	        if(requestCode == 2) {
	        	if (type.equals("foto")){
	        		Uri selectedImageUri = data.getData();
	                //OI FILE Manager
	                filemanagerstring = selectedImageUri.getPath();
	                Log.i("filemanagerstring", filemanagerstring);
	                //MEDIA GALLERY
	                selectedImagePath = getPath(selectedImageUri);
		            /*mRlayoutimage.setBackgroundDrawable(drawable);*/
		            Intent it = new Intent(this, UploadStep3Activity.class);
		    		it.putExtra(Space.class.getName(), space);
		    		it.putExtra(Subject.class.getName(), mSubject);
		    		it.putExtra("id", superId);
		    		it.putExtra("foto", selectedImagePath);
		    		it.putExtra("type", type);
		    		startActivity(it);
		    		super.onActivityResult(requestCode, resultCode, data);
	        	}
	        	if (type.equals("video")){
	        		Uri uriVideo = data.getData();
	        		Log.i("ARQUIVO", getPath(uriVideo));
	        		
	        		Intent it = new Intent(this, UploadStep3Activity.class);
			    	it.putExtra(Space.class.getName(), space);
			    	it.putExtra(Subject.class.getName(), mSubject);
			    	it.putExtra("id", superId);
			    	it.putExtra("video", getPath(uriVideo));
			    	it.putExtra("type", type);
			    	startActivity(it);
			    	super.onActivityResult(requestCode, resultCode, data);
	        		
	        		/*try {
	        		    AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
	        		    FileInputStream fis = videoAsset.createInputStream();
	        		    Date now = new Date();
	        		    File tmpFile = new File(DownloadHelper.getLecturePath(),"video_"+now.getDay()+"_"+now.getMonth()+"_"+now.getHours()+"_"+now.getMinutes()+"_"+now.getSeconds()+".3gp"); 
	        		    FileOutputStream fos = new FileOutputStream(tmpFile);

	        		    byte[] buf = new byte[1024];
	        		    int len;
	        		    while ((len = fis.read(buf)) > 0) {
	        		        fos.write(buf, 0, len);
	        		    }       
	        		    fis.close();
	        		    fos.close();
	        		    
	        		    Intent it = new Intent(this, UploadStep3Activity.class);
			    		it.putExtra(Space.class.getName(), space);
			    		it.putExtra("id", superId);
			    		it.putExtra("video", tmpFile);
			    		it.putExtra("type", type);
			    		startActivity(it);
			    		super.onActivityResult(requestCode, resultCode, data);
	        		    
	        		  } catch (IOException io_e) {
	        		  }*/
	        		
	        	}
	        }
	    }
	}
	
	//UPDATED!
    @SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }
}

