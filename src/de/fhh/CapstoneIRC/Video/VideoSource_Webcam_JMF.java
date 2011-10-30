package de.fhh.CapstoneIRC.Video;



import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;
import javax.media.Manager;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.DataSource;


public class VideoSource_Webcam_JMF implements VideoSource
{
	private static String				defaultVideoDeviceName = "Microsoft WDM Image Capture (Win32)";
	private static String				defaultAudioDeviceName = "DirectSoundCapture";
	private static String				defaultVideoFormatString = "size=320x240, encoding=yuv, maxdatalength=153600";
	private static String				defaultAudioFormatString = "linear, 16000.0 hz, 8-bit, mono, unsigned";

	private static CaptureDeviceInfo	captureVideoDevice = null;
	private static CaptureDeviceInfo	captureAudioDevice = null;
	private static VideoFormat			captureVideoFormat = null;
	private static AudioFormat			captureAudioFormat = null;
	
	@SuppressWarnings("rawtypes")
	private java.util.Vector deviceListVector;
	DataSource data;

	@Override
	public void initializeSource()
	{
		System.out.println("get list of all media devices ...");
		deviceListVector = CaptureDeviceManager.getDeviceList(null);
		if (deviceListVector == null)
		{
			System.err.println("... error: media device list vector is null");
			System.exit(0);
		}
		if (deviceListVector.size() == 0)
		{
			System.err.println("... error: media device list vector size is 0");
			System.exit(0);
		}

		for (int x = 0; x < deviceListVector.size(); x++)
		{
			// display device name
			CaptureDeviceInfo deviceInfo = (CaptureDeviceInfo) deviceListVector.elementAt(x);
			String deviceInfoText = deviceInfo.getName();
			System.out.println("device " + x + ": " + deviceInfoText);

			// display device formats
			Format deviceFormat[] = deviceInfo.getFormats();
			for (int y = 0; y < deviceFormat.length; y++)
			{
				// serach for default video device
				if (captureVideoDevice == null)
					if (deviceFormat[y] instanceof VideoFormat)
					if (deviceInfo.getName().indexOf(defaultVideoDeviceName) >= 0)
				{
					captureVideoDevice = deviceInfo;
					System.out.println(">>> capture video device = " + deviceInfo.getName());
				}

				// search for default video format
				if (captureVideoDevice == deviceInfo)
					if (captureVideoFormat == null)
					if (DeviceInfo.formatToString(deviceFormat[y]).indexOf(defaultVideoFormatString) >= 0)
				{
					captureVideoFormat = (VideoFormat) deviceFormat[y];
					System.out.println(">>> capture video format = " + DeviceInfo.formatToString(deviceFormat[y]));
				}

				// serach for default audio device
				if (captureAudioDevice == null)
					if (deviceFormat[y] instanceof AudioFormat)
					if (deviceInfo.getName().indexOf(defaultAudioDeviceName) >= 0)
				{
					captureAudioDevice = deviceInfo;
					System.out.println(">>> capture audio device = " + deviceInfo.getName());
				}

				// search for default audio format
				if (captureAudioDevice == deviceInfo)
					if (captureAudioFormat == null)
					if (DeviceInfo.formatToString(deviceFormat[y]).indexOf(defaultAudioFormatString) >= 0)
				{
					captureAudioFormat = (AudioFormat) deviceFormat[y];
					System.out.println(">>> capture audio format = " + DeviceInfo.formatToString(deviceFormat[y]));
				}

				System.out.println(" - format: " +  DeviceInfo.formatToString(deviceFormat[y]));
			}
		}
		System.out.println("... list completed.");
	}

	@Override
	public boolean openSource()
	{
		// if we got a camera, use the first available
		if(deviceListVector.size() != 0) 
		{
			CaptureDeviceInfo infoCaptureDevice = (CaptureDeviceInfo) deviceListVector.elementAt (2);
			System.out.println("CaptureDeviceInfo: ");
			System.out.println(infoCaptureDevice.getName());
			System.out.println(infoCaptureDevice.getLocator());
			System.out.println(infoCaptureDevice.getFormats()[0]);

			try
			{
				data = Manager.createDataSource(infoCaptureDevice.getLocator());

			} catch (Exception e)
			{
				System.err.println(e.getMessage());
				e.printStackTrace();
				return false;
			}
			
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
