package de.fhh.CapstoneIRC.Video;

import javax.media.Player;

public interface VideoSource
{
	void initializeSource();
	boolean openSource();
	void closeSource();
	Player getVideoPlayer();
}
