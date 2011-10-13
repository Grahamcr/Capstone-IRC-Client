package de.fhh.CapstoneIRC.Video;

import java.util.List;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.format.RGBFormat;

import net.sf.fmj.ejmf.toolkit.install.PackageUtility;
import net.sf.fmj.media.renderer.video.SimpleAWTRenderer;
import net.sf.fmj.utility.PlugInUtility;

import com.lti.civil.CaptureException;
import com.lti.civil.CaptureSystem;
import com.lti.civil.CaptureSystemFactory;
import com.lti.civil.DefaultCaptureSystemFactorySingleton;

public class VideoSource_Webcam implements VideoSource
{

	@SuppressWarnings("rawtypes")
	private java.util.Vector vectorDevices;
	private CaptureSystem system;
	private Player player;
	
	@Override
	public void initializeSource()
	{
		Format usableFormat = null;
		for (Format f : new SimpleAWTRenderer().getSupportedInputFormats())
		{
			final Format civilOutputFormat = new RGBFormat(null, -1, byte[].class, -1, 24, 1, 2, 3);
			System.out.println(civilOutputFormat.matches(f) + " " + f);
			if(civilOutputFormat.matches(f))
				usableFormat = f;
		}
		if(usableFormat == null)
			usableFormat = new RGBFormat();
		
		PackageUtility.addContentPrefix("net.sf.fmj", false);
		PackageUtility.addProtocolPrefix("net.sf.fmj", false);
		
    	PlugInUtility.registerPlugIn("net.sf.fmj.media.renderer.video.SimpleAWTRenderer");
    	  
		
		
		CaptureSystemFactory factory = DefaultCaptureSystemFactorySingleton.instance();
		@SuppressWarnings("rawtypes")
		List list = null;
		try
		{
			system = factory.createCaptureSystem();
			system.init();
			list = system.getCaptureDeviceInfoList();
		} catch (CaptureException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		}
		for (int i = 0; i < list.size(); ++i)
		{
			com.lti.civil.CaptureDeviceInfo civilInfo = (com.lti.civil.CaptureDeviceInfo) list.get(i);
			//String name, MediaLocator locator, Format[] formats
			CaptureDeviceInfo jmfInfo = new CaptureDeviceInfo(
					//String name, MediaLocator locator, Format[] formats
					civilInfo.getDescription(), new MediaLocator("civil:" + civilInfo.getDeviceID()), new Format[] {usableFormat});
			CaptureDeviceManager.addDevice(jmfInfo);
		}

		vectorDevices = CaptureDeviceManager.getDeviceList(null);
		if (vectorDevices == null)
		{
			System.err.println("CaptureDeviceManager.getDeviceList returned null");
			return;
		}
		if (vectorDevices.size() == 0)
		{
			System.err.println("CaptureDeviceManager.getDeviceList returned empty list");
			return;
		}
	}

	@Override
	public boolean openSource()
	{
		// if we got a camera, use the first available
		if(vectorDevices.size() != 0) 
		{
			CaptureDeviceInfo infoCaptureDevice = (CaptureDeviceInfo) vectorDevices.elementAt (0);
			System.out.println("CaptureDeviceInfo: ");
			System.out.println(infoCaptureDevice.getName());
			System.out.println(infoCaptureDevice.getLocator());
			System.out.println(infoCaptureDevice.getFormats()[0]);

			try
			{
				player = Manager.createPlayer(infoCaptureDevice.getLocator());
			} catch (Exception e)
			{
				System.err.println(e.getMessage());
				e.printStackTrace();
				return false;
			}
	        player.start();
			
		}
		return false;
	}

	@Override
	public void closeSource()
	{
		if(system != null)
			try
			{
				system.dispose();
			} catch (CaptureException e)
			{
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
	}

	@Override
	public Player getVideoPlayer()
	{
		return player;
	}

}
