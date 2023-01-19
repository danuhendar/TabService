
public class CheckThread extends Thread {
	CheckActivity act;
	public CheckThread() {
		act = new CheckActivity();
	}
	
	public void run(){
        while(true){
           try{
        	   act.Run();
        	   Thread.sleep(3000);
           }catch(Exception exc){
               
           }
           
        }
	} 
	
	
	
}
