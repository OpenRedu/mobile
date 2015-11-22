package br.ufpe.cin.openredu.fragments.space;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.developer.redu.DefaultReduClient;
import br.com.developer.redu.models.File;
import br.com.developer.redu.models.Folder;
import br.com.developer.redu.models.Space;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.activities.SpaceActivity;
import br.ufpe.cin.openredu.activities.SpaceActivity.SupportMaterialFragmentListener;
import br.ufpe.cin.openredu.activities.lecture.NewFolderActivity;
import br.ufpe.cin.openredu.activities.lecture.UploadFileFolderActivity;
import br.ufpe.cin.openredu.adapters.SupportMaterialsAdapter;
import br.ufpe.cin.openredu.fragments.NoConnectNotifiableFragment;
import br.ufpe.cin.openredu.util.DownloadHelper;
import br.ufpe.cin.openredu.util.UserHelper;

@SuppressLint("ValidFragment")
public class SupportMaterialFragment extends NoConnectNotifiableFragment {
	public static final String EXTRAS_FOLDER = "EXTRAS_FOLDER";
	public static final String EXTRAS_SPACE = "EXTRAS_SPACE";

	private Space mSpace;

	private List<Folder> folders;
	private List<File> files;
	private Folder mFolder;

	ProgressDialog mProgressDialog;
	ProgressDialog mProgressdialogRemove;
	private String folderRaizID;

	DownloadFile df;

	private ProgressBar mProgressBar;

	ListView lvFiles;
	private SupportMaterialFragmentListener mListener;
	RelativeLayout rlIndice;

	Dialog dialog;

	private SupportMaterialsAdapter mAdapter;

	public TextView mTvEmpytMsg;

	public SupportMaterialFragment() {
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View v = inflater.inflate(R.layout.fragment_support_material,
				container, false);
		TextView indice;
		ImageButton ibBack;

		Bundle args = getArguments();
		if (args != null) {
			mFolder = (Folder) args.get(EXTRAS_FOLDER);
			mSpace = (Space) args.get(EXTRAS_SPACE);
		}

		mProgressBar = (ProgressBar) v.findViewById(R.id.pb);
		mTvEmpytMsg = (TextView) v.findViewById(R.id.elv_subject_empyt);
		rlIndice =(RelativeLayout)v.findViewById(R.id.rlIndice);
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage("Aguarde…");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCanceledOnTouchOutside(false);

		mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						df.cancel(true);
						df.running = false;
						dialog.dismiss();
					}
				});

		if (mFolder != null) {
			indice = (TextView) v.findViewById(R.id.tvIndice);
			indice.setText(mFolder.name);
			ibBack = (ImageButton) v.findViewById(R.id.ibBack);
			ibBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.onBackToPreviousFragment(mFolder);
				}
			});
		} else {
			ibBack = (ImageButton) v.findViewById(R.id.ibBack);
			ibBack.setVisibility(View.GONE);
		}

		String role = UserHelper.getUserRoleInCourse(getActivity());
		if (role.equals("teacher") || role.equals("environment_admin") || role.equals("tutor")) {
			mTvEmpytMsg.setText("Esta pasta está vazia, comece adicionando algo nela através do '+' ao lado do seu nome.");
			ImageButton ibMore = (ImageButton) v.findViewById(R.id.ibMore);
			ibMore.setVisibility(View.VISIBLE);
			ibMore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent it = new Intent(getActivity(),
							UploadFileFolderActivity.class);
					it.putExtra(Space.class.getName(), mSpace);
					if (mFolder != null)
						it.putExtra("id", mFolder.id);
					else
						it.putExtra("id", folderRaizID);
					startActivity(it);
				}
			});
		}

		lvFiles = (ListView) v.findViewById(R.id.lvFiles);
		registerForContextMenu(lvFiles);

		lvFiles.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Object obj = lvFiles.getItemAtPosition(position);
				if (obj instanceof Folder) {
					Folder folder = (Folder) lvFiles
							.getItemAtPosition(position);
					mListener.onSwitchToNextFragment(folder);
				} else {
					File file = (File) lvFiles.getItemAtPosition(position);
					File[] files = { file };
					java.io.File f = new java.io.File(DownloadHelper
							.getSupportMaterialPath(), file.name);
					if (f.exists()) {
						Intent it;
						try {
							it = DownloadHelper.loadDocInReader(f);
							startActivity(it);
						} catch (ActivityNotFoundException e) {
	  		                Toast.makeText(getActivity(), 
			                "Arquivo não suportado. Instale um aplicativo capaz de abrir o Arquivo: "+file.mimetype , 
			                Toast.LENGTH_LONG).show();
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}

					} else {
						df = new DownloadFile();
						df.execute(files);
					}

				}
			}

		});

		new LoadFoldersAndFilesTask().execute();

		return v;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Object row = (Object) mAdapter.getItem(info.position);
		switch (item.getItemId()) {
		case R.id.edit:
			if (row instanceof Folder) {
				editFolder((Folder) row);
			}
			return true;
		case R.id.delete:
			if (row instanceof Folder) {
				Folder f = (Folder) row;
				Log.i("FOLDER", f.id);
				deleteFolder((Folder) row);
			}
			if (row instanceof File) {
				deleteFile((File) row);
			}
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo ma = (AdapterContextMenuInfo) menuInfo;
		Object obj = lvFiles.getAdapter().getItem(ma.position);
		if (obj instanceof Folder) {
			getActivity().getMenuInflater().inflate(R.drawable.menu, menu);
		}else{
			getActivity().getMenuInflater().inflate(R.drawable.menu_file, menu);
		}
	}

	private void deleteFile(File file) {
		new RemoveFile().execute(file.id);
	}

	private void deleteFolder(Folder folder) {
		new RemoveFolder().execute(folder.id);
	}

	private void editFolder(Folder folder) {
		Intent it = new Intent(getActivity(), NewFolderActivity.class);
		it.putExtra(Space.class.getName(), mSpace);
		it.putExtra(Folder.class.getName(), folder);
		startActivity(it);
	}

	public void setListener(SupportMaterialFragmentListener listener) {
		mListener = listener;
	}

	class LoadFoldersAndFilesTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... params) {
			try {
				DefaultReduClient redu = ReduApplication
						.getReduClient(getActivity());
				if (mFolder == null) {
					folderRaizID = redu.getFolderID(mSpace.id);
				} else {
					folderRaizID = mFolder.id;
				}
				folders = redu.getFolders(folderRaizID);
				folders.removeAll(Collections.singleton(null));
				files = redu.getFilesByFolder(folderRaizID);
				files.removeAll(Collections.singleton(null));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (getActivity() != null) {
				mAdapter = new SupportMaterialsAdapter(getActivity(), folders,
						files);
				if (mAdapter.getCount() != 0) {
					lvFiles.setAdapter(mAdapter);
					rlIndice.setVisibility(View.VISIBLE);
					mProgressBar.setVisibility(View.GONE);
				} else {
					rlIndice.setVisibility(View.VISIBLE);
					mTvEmpytMsg.setVisibility(View.VISIBLE);
					mProgressBar.setVisibility(View.GONE);
				}
			}
		};
	}

	// usually, subclasses of AsyncTask are declared inside the activity class.
	// that way, you can easily modify the UI thread from here
	private class DownloadFile extends AsyncTask<File, Integer, java.io.File> {

		private volatile boolean running = true;

		@Override
		protected java.io.File doInBackground(File... file) {
			try {

				String path = file[0].getFilePath();
				URL url = new URL(path);

				String[] temp = path.split("\\?")[0].split("/");
				String fileName = temp[temp.length - 1];

				URLConnection connection = url.openConnection();
				connection.connect();
				// this will be useful so that you can show a typical 0-100%
				// progress bar
				int fileLength = connection.getContentLength();

				String newFolder = DownloadHelper.getSupportMaterialPath();
				java.io.File myNewFolder = new java.io.File(newFolder);
				if (!myNewFolder.exists())
					myNewFolder.mkdirs();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream());
				java.io.File filling = new java.io.File(newFolder, fileName);
				OutputStream output = new FileOutputStream(filling);

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					if (!running) {
						output.flush();
						output.close();
						input.close();
						filling.delete();
						return null;
					}
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
				return filling;
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog.show();
		}

		@Override
		protected void onCancelled() {
			running = false;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			mProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(java.io.File file) {
			super.onPostExecute(file);
			publishProgress(0);
			mProgressDialog.dismiss();
			if (getActivity() != null && file != null) {
				try {
					Intent it = DownloadHelper.loadDocInReader(file);
					startActivity(it);
				} catch (ActivityNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	class RemoveFolder extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			mProgressdialogRemove = ProgressDialog.show(getActivity(), "Redu",
					"Removendo Pasta...", false, true);
			mProgressdialogRemove.setIcon(R.drawable.ic_launcher);
			mProgressdialogRemove.setCancelable(false);
			super.onPreExecute();
		}

		protected Void doInBackground(String... text) {

			DefaultReduClient redu = ReduApplication
					.getReduClient(getActivity());
			redu.deleteFolder(text[0]);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mProgressdialogRemove.dismiss();
			mAdapter.notifyDataSetChanged();
			SpaceActivity activity = (SpaceActivity) getActivity();
			activity.onRestart();
		};
	}
	
	class RemoveFile extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			mProgressdialogRemove = ProgressDialog.show(getActivity(), "Redu",
					"Removendo Arquivo...", false, true);
			mProgressdialogRemove.setIcon(R.drawable.ic_launcher);
			mProgressdialogRemove.setCancelable(true);
			super.onPreExecute();
		}

		protected Void doInBackground(String... text) {

			DefaultReduClient redu = ReduApplication
					.getReduClient(getActivity());
			redu.deleteFile(text[0]);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mProgressdialogRemove.dismiss();
			mAdapter.notifyDataSetChanged();
			SpaceActivity activity = (SpaceActivity) getActivity();
			activity.onRestart();
		};
	}

	@Override
	public void onNoConnectionAlertClicked() {
		new LoadFoldersAndFilesTask().execute();
	}

}
