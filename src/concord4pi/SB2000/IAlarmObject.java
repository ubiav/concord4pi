package concord4pi.SB2000;

import java.util.Date;

public interface IAlarmObject {

	public void setState(String action, int state);
	public AlarmObjectStatus getCurrentState();
	public Date getCurrentStateTimeStamp();
	public AlarmObjectStatus getLastState();
	public Date getLastActiveStateTimeStamp();	
	public boolean is(String number);
	public String getID();
	public void setParent(Object newParent);
	public Object getParent();
	public Object find(String id);
	public boolean exists(String id);
	public void add(AlarmObject newObject);
	public void setPrevious(AlarmObject previousObject);
	public void setNext(AlarmObject nextObject);
	public AlarmObject getNext();
	public AlarmObject getPrevious();
	public void clear(String id);
	public void clean();
	public String toString();
	public int getLength();
	
}
