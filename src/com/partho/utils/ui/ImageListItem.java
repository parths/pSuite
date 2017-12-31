package com.partho.apps.filebrowser;

/**
 * Defines a list item that contains an image and text describing the item
 */
public class ImageListItem implements Comparable<ImageListItem>
{

	private String name;
	private String icon;

	public ImageListItem(String name, String icon)
	{
		this.name = name;
		this.icon = icon;
	}
	
	public String getName()
	{
		return name;
	}

	public String getIcon()
	{
		return icon;
	}

	public int compareTo(ImageListItem item)
	{
		if(this.name != null)
			return this.name.toLowerCase().compareTo(item.getName().toLowerCase());
		else
			throw new IllegalArgumentException();
	}
}
