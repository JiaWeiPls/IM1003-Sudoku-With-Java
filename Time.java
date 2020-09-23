public class Time {
	private int hour, minute, second, milli;

	public Time(int hour, int minute, int second) {
		this.hour=hour;
		this.minute=minute;
		this.second=second;
		this.milli=0;
	}	
	public void setTime(int hour, int minute, int second) {
		this.hour=hour;
		this.minute=minute;
		this.second=second;
	}	
	public int getHour() {
		return hour;
	}
	public int getMinute() {
		return minute;
	}	
	public int getSecond() {
		return second;
	}	
	public void setHour(int hour) {
		this.hour=hour;
	}	
	public void setMinute(int minute) {
		this.minute=minute;
	}	
	public void setSecond(int second) {
		this.second=second;
	}	
	public String toString() {
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}
	// Advance "this" instance by 1 second and return "this" instance

	public Time nextSecond() {  // to return a Time instance

	   ++second;

	   if (second == 60) {

    	  second = 0;

	      ++minute;

    	  if (minute == 60) {

        	 minute = 0;

	         ++hour;

    	     if (hour == 24) {

        	    hour = 0;

	         }

    	  }

	   }

	   return this;  // return this instance to support chaining operation

	}	

	public Time nextMilli() {  // to return a Time instance

	   ++milli;
	   if (milli == 100) {
	   		milli = 0;
	   		++second;


	   if (second == 60) {

    	  second = 0;

	      ++minute;

    	  if (minute == 60) {

        	 minute = 0;

	         ++hour;

    	     if (hour == 24) {

        	    hour = 0;

	         }

    	  }

	   }
	}

	   return this;  // return this instance to support chaining operation

	}
	public Time previousSecond() {  // to return a Time instance

	   --second;

	   if (second == -1) {

    	  second = 59;

	      --minute;

    	  if (minute == -1) {

        	 minute = 59;

	         --hour;

    	     if (hour == -1) {

        	    hour = 23;

	         }

    	  }

	   }

	   return this;  // return this instance to support chaining operation

	}
}