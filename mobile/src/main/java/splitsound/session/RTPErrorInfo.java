package splitsound.session;

/**
 * Created by J. McKernan on 2/25/2018.
 */

public class RTPErrorInfo
	{
		public int code;
		public String description;
		
		public RTPErrorInfo(int code, String description)
		{
			this.code = code;
			this.description = description;
		}
	}
