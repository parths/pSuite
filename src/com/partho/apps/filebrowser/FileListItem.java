package com.partho.apps.filebrowser;

public class FileListItem implements Comparable<FileListItem>
{

	private String name;
	private String path;
	private String info;
	private String icon;
	private String dateTime;

	public FileListItem(String name, String path, String info, String icon, String dateTime)
	{
		this.name = name;
		this.path = path;
		this.info = info;
		this.icon = icon;
		this.dateTime = dateTime;
	}
	
	public String getName()
	{
		return name;
	}

	public String getPath()
	{
		return path;
	}

	public String getInfo()
	{
		return info;
	}

	public String getIcon()
	{
		return icon;
	}

	public String getDateTime()
	{
		return dateTime;
	}

	public int compareTo(FileListItem item)
	{
		if(this.name != null)
			return this.name.toLowerCase().compareTo(item.getName().toLowerCase());
		else
			throw new IllegalArgumentException();
	}
}
