
public class ThreadSQL extends Thread {
	TabService idm;
     
    public ThreadSQL(int num){
    	idm = new TabService();
    }
    
    public void run(){
        for(int l = 0;l<1;l++){
           try{
        	   idm.Run();
           }catch(Exception exc){
               
           }
           
        }
    } 
}
