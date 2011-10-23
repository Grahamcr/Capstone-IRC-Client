package de.fhh.CapstoneIRC.Video;

import javax.media.protocol.DataSource;

public interface VideoSource
{
	void initializeSource();
	boolean openSource();
	void closeSource();
	DataSource getDataSource();
}
