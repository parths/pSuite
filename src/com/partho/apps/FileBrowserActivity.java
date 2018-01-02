package com.partho.apps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.Button;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import com.partho.apps.filebrowser.FileArrayAdapter;
import com.partho.apps.filebrowser.FileListItem;


/**
 * Ref: 
 * 		http://stackoverflow.com/questions/9340332/how-can-i-get-the-list-of-mounted-external-storage-of-android-device
 * 		https://developer.android.com/reference/java/io/File.html
 * 		https://developer.android.com/reference/android/os/Environment.html
 * 		NOTES:
 * 			- Environment.getExternalStorageDirectory should give the root path for the mounted external storage.
 * 			e.g. '/mnt/sdcard/' or '/mnt/sdcard/'.
 * 			right now we can traverse and get to the root, but should try to get a list of all storage devices.
 * 			e.g. from /proc/mounts or using other android values.
 * 			Check: 
 * 			http://stackoverflow.com/questions/39725237/get-all-storage-paths-in-android 
 * 			and 
 * 			http://stackoverflow.com/questions/39850004/android-get-all-external-storage-path-for-all-devices
 */
public class FileBrowserActivity extends Activity
{

	public enum BrowserMode
	{
		BROWSE(0), 
		SELECT_DIRECTORY(1), 
		SELECT_FILE(2);
		
		private int bmValue;
		private static HashMap bmMap = new HashMap();
		
		static
		{
			for(BrowserMode bm: BrowserMode.values())
			{
				bmMap.put(bm.bmValue, bm);
			}
		}
		
		BrowserMode(int val)
		{
			bmValue = val;
		}
		
		public int getValue()
		{
			return bmValue;
		}
		
		public static BrowserMode fromInt(int intVal)
		{
			return (BrowserMode)bmMap.get(intVal);
		}
	};
	
	public static final String BROWSER_MODE_EXTRA_KEY = "BrowserMode";
	public static final String BROWSER_RETURN_DIR_PATH = "DirectoryPath";
	public static final String BROWSER_RETURN_FILE_NAME = "FileName";

	private ListView listView;
	private File currentDirectory;
	private FileArrayAdapter arrayAdapter;
	private BrowserMode mBrowserMode = BrowserMode.BROWSE;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_browser_main);
		listView = (ListView) findViewById(R.id.file_list);
		setupActivity();
    }
	
	private void setupActivity()
	{
		Intent intent = getIntent();
		int value = intent.getIntExtra(BROWSER_MODE_EXTRA_KEY, -1);		// default to BrowserMode.BROWSE, -1 is for testing
		if(value < BrowserMode.BROWSE.getValue() || value > BrowserMode.SELECT_FILE.getValue())
			value = BrowserMode.BROWSE.getValue();
		mBrowserMode = BrowserMode.fromInt(value);
		// Need to refine this. Check NOTES
		if(isExternalStorageAvailable())
			currentDirectory = Environment.getExternalStorageDirectory();
		else
			currentDirectory = new File("/");
		SetupBrowseMode();
		populateFileList(currentDirectory);
	}
	
	private void SetupBrowseMode()
	{
		Button btnSelFolder = (Button)findViewById(R.id.btn_select_folder);
		if(mBrowserMode != BrowserMode.SELECT_DIRECTORY)
		{
			btnSelFolder.setVisibility(View.GONE);
		}
		else
		{
			btnSelFolder.setVisibility(View.VISIBLE);
			btnSelFolder.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						onFolderSelected();
					}
				}
			);
		}
	}
	
	private boolean isExternalStorageAvailable()
	{
		String extStorageState = Environment.getExternalStorageState();
		
		System.out.println("External media state: " + extStorageState);
		if(extStorageState.equals(Environment.MEDIA_MOUNTED) || extStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
			System.out.println(Environment.getExternalStorageDirectory());
		
		return (extStorageState.equals(Environment.MEDIA_MOUNTED) || extStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY));
	}
	
	private void populateFileList(File f)
	{
		this.setTitle(f.getAbsolutePath());
		File[] allFiles = f.listFiles();
		List<FileListItem> dirListing = collectDirListing(allFiles, f);
		populateListView(dirListing);
	}
	
	private void populateListView(List<FileListItem> dirListing)
	{
		arrayAdapter = new FileArrayAdapter(FileBrowserActivity.this, R.layout.file_browser_list_item, dirListing);
		listView.setAdapter(arrayAdapter);		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
			{
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					onListItemClick(parent, view, position, id);
				}
			});
	}
	
	private List<FileListItem> collectDirListing(File[] allFiles, File f)
	{
		List<FileListItem> dirs = new ArrayList<FileListItem>();
		List<FileListItem> files = new ArrayList<FileListItem>();
		
		if(allFiles != null)
		{
			for(int i = 0; i < allFiles.length; ++i)
				populateFileOrDirectoryItem(allFiles[i], dirs, files);
		}
		Collections.sort(dirs);
		Collections.sort(files);
		dirs.addAll(files);
		if(!f.getName().equalsIgnoreCase("sdcard") && !f.getName().equalsIgnoreCase("/") )
			dirs.add(0, new FileListItem("..", f.getParent(), "", "directory_up", "Parent Directory"));
		return dirs;
	}
	
	private void populateFileOrDirectoryItem(File f, List<FileListItem> dirs, List<FileListItem> files)
	{
		try
		{
			DateFormat formatter = DateFormat.getDateTimeInstance();
			Date lastModifiedDate = new Date(f.lastModified());
			String date_modified = formatter.format(lastModifiedDate);
			if(f.isDirectory())
			{
				File[] fileList = f.listFiles();
				String numFiles = "";
				if(fileList != null)
					numFiles = fileList.length + " Files";
				else
					numFiles = "Empty";
				dirs.add(new FileListItem(f.getName(), f.getAbsolutePath(), numFiles, "icon_folder_01", date_modified));
			}
			else
			{
				files.add(new FileListItem(f.getName(), f.getAbsolutePath(), f.length() + "b", "icon_file_00", date_modified));
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
		}
	}
	
	protected void onListItemClick(AdapterView<?> parent, View v, int position, long id)
	{
		FileListItem item = arrayAdapter.getItem(position);
		if(item.getIcon().equalsIgnoreCase("icon_folder_01") || item.getIcon().equalsIgnoreCase("directory_up"))
		{
			currentDirectory = new File(item.getPath());
			populateFileList(currentDirectory);
		}
		else
		{
			onFileClick(item);
		}
	}
	
	private void onFolderSelected()
	{
		if(mBrowserMode != BrowserMode.SELECT_DIRECTORY)
			return;
		Toast.makeText(this, "Folder Clicked: " + currentDirectory, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.putExtra(BROWSER_RETURN_DIR_PATH, currentDirectory.toString());
		setResult(RESULT_OK, intent);
		System.gc();
		finish();
	}
	
	private void onFileClick(FileListItem item)
	{
		if(mBrowserMode != BrowserMode.SELECT_FILE)
			return;
		Toast.makeText(this, "Folder Clicked: " + currentDirectory, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.putExtra(BROWSER_RETURN_DIR_PATH, currentDirectory.toString());
		intent.putExtra(BROWSER_RETURN_FILE_NAME, item.getName());
		setResult(RESULT_OK, intent);
		System.gc();
		finish();
	}
}
