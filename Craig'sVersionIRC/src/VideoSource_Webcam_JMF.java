 
/**
 * @author Julian Junghans
 * This Class connects to the webcam and supplies a DataStream
 * This is the implementention via JMF
 */


import java.io.IOException;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;
import javax.media.Manager;
import javax.media.NoDataSourceException;
import javax.media.format.VideoFormat;
import javax.media.protocol.DataSource;


public class VideoSource_Webcam_JMF implements VideoSource
{	
	@SuppressWarnings("rawtypes")
	private java.util.Vector deviceListVector;
	DataSource data;

	@Override
	public boolean initializeSource()
	{
		System.out.println("get list of all media devices ...");
		try
		{
			deviceListVector = CaptureDeviceManager.getDeviceList(new VideoFormat(null));
			if (deviceListVector == null)
			{
				System.err.println("... error: media device list vector is null");
				return false;
			}
			if (deviceListVector.size() == 0)
			{
				System.err.println("... error: media device list vector size is 0");
				return false;
			}
	
			for (int x = 0; x < deviceListVector.size(); x++)
			{
				// display device name
				CaptureDeviceInfo deviceInfo = (CaptureDeviceInfo) deviceListVector.elementAt(x);
				String deviceInfoText = deviceInfo.getName();
				if(deviceInfoText.equals("DirectSoundCapture") || deviceInfoText.equals("JavaSound audio capture"))
					continue;
				System.out.println("device " + x + ": " + deviceInfoText);
	
				// display device formats
				Format deviceFormat[] = deviceInfo.getFormats();
				for (int y = 0; y < deviceFormat.length; y++)
				{
					System.out.println(" - format: " +  DeviceInfo.formatToString(deviceFormat[y]));
				}
			}
			System.out.println("... list completed.");
			return true;
		}
		catch(Exception e)
		{
			System.err.println(e.getLocalizedMessage());
			System.out.println("Continue without Webcam...");
			return false;
		}
	}

	@Override
	public boolean openSource() throws NoDataSourceException, IOException
	{
		// if we got a camera, use the first available
		if(deviceListVector.size() != 0) 
		{
			CaptureDeviceInfo infoCaptureDevice = (CaptureDeviceInfo) deviceListVector.elementAt (0);
			System.out.println("CaptureDeviceInfo: ");
			System.out.println(infoCaptureDevice.getName());
			System.out.println(infoCaptureDevice.getLocator());
			System.out.println(infoCaptureDevice.getFormats()[0]);
			
			data = Manager.createDataSource(infoCaptureDevice.getLocator());
			if(data == null)
				return false;
		}
		return true;
	}

	@Override
	public void closeSource()
	{
		if(data != null)
		{
			data.disconnect();
			data = null;
		}
	}

	@Override
	public DataSource getDataSource()
	{
		return data;
	}

}
