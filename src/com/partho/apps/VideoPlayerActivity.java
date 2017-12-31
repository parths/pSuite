package com.partho.apps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.media.MediaPlayer;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.Toast;
import android.provider.MediaStore;
import android.database.Cursor;
import java.io.File;
import java.io.FilenameFilter;


public class VideoPlayerActivity extends Activity
{

	private final int FILEBROWSER_REQ_CODE = 12346;
	private final int MEDIA_PICK_REQ_CODE = 12345;

	private MediaPlayer mPlayer = null;
	private File[] videoFiles = null;
	private int currentIndex = -1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_main);
		selectFile();
    }
	
	@Override
	public void onStop()
	{
		super.onStop();
		releasePlayer();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		releasePlayer();
	}
	
	private void releasePlayer()
	{
		if(mPlayer != null)
			mPlayer.release();
		mPlayer = null;
	}

    public void selectFile()
    {
		videoFiles = null;
		currentIndex = -1;
		try
		{
			OpenFileBrowserIntent();
		}
		catch(java.lang.ClassNotFoundException cnfe)
		{
			System.out.println("Class Not Found: com.partho.apps.FileBrowserActivity");
			OpenMediaPicker();
		}
    }
	
	private void OpenFileBrowserIntent() throws java.lang.ClassNotFoundException
	{
		Intent intent = new Intent(this, Class.forName("com.partho.apps.FileBrowserActivity"));
		intent.putExtra("BrowserMode", 1);
		startActivityForResult(intent, FILEBROWSER_REQ_CODE);
	}
	
	private void OpenMediaPicker()
	{
		Intent intent = new Intent();
		intent.setType("video/*");
		intent.setAction(Intent.ACTION_PICK);
		startActivityForResult(Intent.createChooser(intent, "Select Video:"), MEDIA_PICK_REQ_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(requestCode == MEDIA_PICK_REQ_CODE)
		{
			if (resultCode == RESULT_OK) 
			{
				Uri selectedVideoUri = data.getData();
				playVideo(selectedVideoUri);
			}
		}
		else if(requestCode == FILEBROWSER_REQ_CODE)
		{
			if (resultCode == RESULT_OK) 
			{
				String path = data.getStringExtra("DirectoryPath");
				System.out.println("Got Path: " + path);
				getVideoFilesInDirectory(path);
				playNextVideo();
			}
		}
	}
	
	private void playNextVideo()
	{
		Uri videoUri = getNextVideo();
		if(videoUri != null)
			playVideo(videoUri);
		else
			Toast.makeText(this, "No more videos", Toast.LENGTH_SHORT).show();
	}
	
	private Uri getNextVideo()
	{
		if((currentIndex < 0) || (videoFiles == null) || (currentIndex >= videoFiles.length))
			return null;
		Uri videoUri = Uri.fromFile(videoFiles[currentIndex]);
		currentIndex++;
		return videoUri;
	}
	
	private void playVideo(Uri videoUri)
	{
		try
		{
			Toast.makeText(this, "Playing next... " + videoUri.getEncodedPath(), Toast.LENGTH_SHORT).show();
			VideoView videoView = (VideoView)findViewById(com.partho.apps.R.id.video_view);
			MediaController mediaCtrl = new MediaController(this);
			mediaCtrl.setAnchorView(videoView);
			videoView.setMediaController(mediaCtrl);
			videoView.setVideoURI(videoUri);
			videoView.requestFocus();
			videoView.setOnCompletionListener( new MediaPlayer.OnCompletionListener() 
				{
					@Override
					public void onCompletion(MediaPlayer mp)
					{
						playNextVideo();
					}
				});
			videoView.setOnPreparedListener( new MediaPlayer.OnPreparedListener()
				{
					@Override
					public void onPrepared(MediaPlayer mp)
					{
					}
				});
			videoView.setOnErrorListener( new MediaPlayer.OnErrorListener()
				{
					@Override
					public boolean onError(MediaPlayer mp, int err, int extra)
					{
						System.out.println("Error: " + err + " " + extra);
						return false;
					}
				});
			videoView.start();
		}
		catch(Exception e)
		{
			playNextVideo();
		}
	}
	
	private void getVideoFilesInDirectory(String dirPath)
	{
		videoFiles = (new File(dirPath)).listFiles( new FilenameFilter()
			{
				@Override
				public boolean accept(File dir, String fileName)
				{
					// Arbitrary list of extensions.
					String[] formats = { ".mp4", ".3gp", ".wmv", ".avi", ".avi", ".mov", ".ogg", ".mpg", ".mpeg", ".mp2", "m4v" };
					boolean canAccept = false;
					for(int i = 0; i < formats.length; ++i)
					{
						if(fileName.endsWith(formats[i]) && !fileName.startsWith("."))
						{
							canAccept = true;
							break;
						}
					}
					return canAccept;
				}
			});
		currentIndex = 0;
		
		// TODO: Two options that didn't work. Need to figure out why.
/*		Uri dirUri = Uri.fromFile(new File(dirPath));
		String[] proj = { MediaStore.Video.VideoColumns.DATA };
		Cursor c = getContentResolver().query(dirUri, proj, null, null, null);
		
		if(c != null)
		{
			int cnt = c.getCount();
			while(c.moveToNext())
			{
				System.out.println("VIDEO: " + c.getString(0));
			}
		}
		else
		{
		}*/
/*		String[] proj = { MediaStore.Files.FileColumns.DATA };
		String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
		CursorLoader loader = new CursorLoader(this, dirUri, projection, selection, null);
		Cursor c = loader.loadInBackground();
		if(c != null)
		{
			int cnt = c.getCount();
			System.out.println("Count: " + cnt);
			while(c.moveToNext())
			{
				System.out.println("VIDEO: " + c.getString(0));
			}
		}*/		
	}
	
}
