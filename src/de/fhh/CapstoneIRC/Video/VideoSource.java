package de.fhh.CapstoneIRC.Video;

import java.io.IOException;

import javax.media.NoDataSourceException;
import javax.media.protocol.DataSource;

public interface VideoSource
{
	boolean initializeSource();
	boolean openSource() throws NoDataSourceException, IOException;
	void closeSource();
	DataSource getDataSource();
}
