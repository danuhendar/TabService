import java.io.FileInputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;




public class TabService {
	String maxvmusepercent;
    String cleansession;
    boolean res_cleansession = false;
    String keepalive;
    int res_keepalive = 70;
    String reconnect;
    boolean res_reconnect = false;
    String limit_read;
    boolean res_limit_read = false;
    String flag_read_initial;
    boolean res_flag_read_initial = false;
    String read_command_topic_hastag;
    boolean res_read_command_topic_hastag = false;
    MqttClient client_transreport_login;
    MqttClient client_transreport;
    String will_retained;
    boolean res_will_retained = false;
    String ip_mongo_db;
    Global_function gf = new Global_function(true);
    Interface_ga inter_login;
    Connection con;
    SQLConnection sqlcon = new SQLConnection();
    int counter = 1;
    
	public TabService() {
		 
	}
	
	
	
	String Parser_TASK,
    Parser_ID,
    Parser_SOURCE,
    Parser_COMMAND,
    Parser_OTP,
    Parser_TANGGAL_JAM,
    Parser_VERSI,
    Parser_HASIL,
    Parser_FROM,
    Parser_TO,
    Parser_SN_HDD,
    Parser_IP_ADDRESS,
    Parser_STATION,
    Parser_CABANG,
    Parser_NAMA_FILE,
    Parser_CHAT_MESSAGE,
    Parser_REMOTE_PATH,
    Parser_LOCAL_PATH,
    Parser_SUB_ID; 
    
    public void UnpackJSON(String json_message){
        
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(json_message);
        } catch (org.json.simple.parser.ParseException ex) {
            System.out.println("message json : "+json_message);
            System.out.println("message error : "+ex.getMessage());
            //ex.printStackTrace();
            //Logger.getLogger(IDMReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Parser_TASK = obj.get("TASK").toString();
        } catch (Exception ex) {
             Parser_TASK = "";
        }       
        try{
            Parser_ID = obj.get("ID").toString();
        }catch(Exception exc){
            Parser_ID = "";
        }
        try{
            Parser_SOURCE = obj.get("SOURCE").toString();
        }catch(Exception exc){
            Parser_SOURCE = "";
        }
        try{
            Parser_COMMAND = obj.get("COMMAND").toString();
        }catch(Exception exc){
            Parser_COMMAND = "";
        }
          try{
           Parser_OTP = obj.get("OTP").toString();
        }catch(Exception exc){
            Parser_OTP = "";
        }
        
        
        try{
           Parser_TANGGAL_JAM = obj.get("TANGGAL_JAM").toString();
        }catch(Exception exc){
            Parser_TANGGAL_JAM = "";
        }
        try{
            Parser_VERSI = obj.get("RESULT").toString().split("_")[7];
        }catch(Exception exc){
            try{
                Parser_VERSI = obj.get("VERSI").toString();
            }catch(Exception exc1){ Parser_VERSI = "";}
            
        }

        try{
            Parser_HASIL = obj.get("HASIL").toString();
            Parser_FROM = obj.get("FROM").toString();
            Parser_TO = obj.get("TO").toString();

        }catch(Exception exc){
            Parser_HASIL = "";
            Parser_FROM = "";
            Parser_TO = "";
        }
       
        try{
            Parser_SN_HDD = obj.get("SN_HDD").toString();
        }catch(Exception exc){
            try{
                Parser_SN_HDD = obj.get("SN_HDD").toString();
            }catch(Exception exc1){Parser_SN_HDD = "";}
            
        }
        try{
            Parser_IP_ADDRESS = obj.get("IP_ADDRESS").toString();
        }catch(Exception exc){
            try{
                Parser_IP_ADDRESS = obj.get("IP_ADDRESS").toString();    
            }catch(Exception exc1){
                Parser_IP_ADDRESS = "";
            }

        }
        
        try{
            Parser_STATION = obj.get("STATION").toString();
        }catch(Exception exc){
            try{
                Parser_STATION = obj.get("STATION").toString();
            }catch(Exception exc1){Parser_STATION = "";}
            
        }
        
        try{
           Parser_CABANG = obj.get("CABANG").toString();
        }catch(Exception exc){
            try{
                Parser_CABANG = obj.get("CABANG").toString();
            }catch(Exception exc1){Parser_CABANG = "";}
        }
        
        try{
            Parser_NAMA_FILE = obj.get("NAMA_FILE").toString();
        }catch(Exception exc){
            Parser_NAMA_FILE = "";
        }
        try{
            Parser_CHAT_MESSAGE = obj.get("CHAT_MESSAGE").toString();
        }catch(Exception exc){
            Parser_CHAT_MESSAGE = "";
        }
        try{
            Parser_REMOTE_PATH = obj.get("REMOTE_PATH").toString();
        }catch(Exception exc){
            Parser_REMOTE_PATH = "";
        }
        try{
            Parser_LOCAL_PATH = obj.get("LOCAL_PATH").toString();
        }catch(Exception exc){
            Parser_LOCAL_PATH = "";
        }
        try{
            Parser_SUB_ID = obj.get("SUB_ID").toString();
        }catch(Exception exc){
            Parser_SUB_ID = "";
        }
        
     }
     
   
    
    public void cekAktif(int qos_message_command,String topic_config) {
    	try {
    		String rtopic_config = topic_config;
    		System.out.println("SUBS : "+rtopic_config);
            client_transreport.subscribe(rtopic_config,qos_message_command,new IMqttMessageListener() {
                @Override
                public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                   
                                Date HariSekarang_run = new Date();
                                String payload = new String(message.getPayload());

                                String msg_type = "";
                                String message_ADT_Decompress = "";
                                try{
                                    message_ADT_Decompress = gf.ADTDecompress(message.getPayload());
                                    msg_type = "json";
                                }catch(Exception exc){
                                    message_ADT_Decompress = payload;
                                    msg_type = "non json";
                                }
                                
                                String tanggal_jam = gf.get_tanggal_curdate_curtime();
                                gf.WriteFile("timemessage.txt", "", tanggal_jam, false);
                               

                                counter++;
                                UnpackJSON(message_ADT_Decompress);
                                gf.WriteLog("OpenTab : "+message_ADT_Decompress, true);
                                //System.out.println(message_ADT_Decompress);
                                gf.PrintMessage2("RECV > "+rtopic_config+"",counter,msg_type,topic,Parser_TASK,Parser_FROM,Parser_TO,null,HariSekarang_run);
                                if(Parser_SOURCE.equals("IDMCommandListeners")) {
                                	//System.out.println(message_ADT_Decompress);
                                	gf.InsTransReport(Parser_TASK,
                        					Parser_ID,
                        					Parser_SOURCE,
                        					Parser_COMMAND,
                        					Parser_OTP,
                        					Parser_TANGGAL_JAM,
                        					Parser_VERSI,
                        					Parser_HASIL,
                        					Parser_FROM,
                        					Parser_TO,
                        					Parser_SN_HDD,
                        					Parser_IP_ADDRESS,
                        					Parser_STATION,
                        					Parser_CABANG,
                        					Parser_NAMA_FILE,
                        					Parser_CHAT_MESSAGE,
                        					Parser_REMOTE_PATH,
                        					Parser_LOCAL_PATH,
                        					Parser_SUB_ID,
                        					Boolean.parseBoolean(gf.en.getTampilkan_query_console()),
                        					"REPLACE",
                        					"transaksi_aktivitas_user");
                                }
                                
                }
            });
    	}catch(Exception exc) {}
    }
    
    
    public void closeTab(int qos_message_command,String topic_config) {
    	try {
    		String rtopic_config = topic_config;
    		System.out.println("SUBS : "+rtopic_config);
            client_transreport.subscribe(rtopic_config,qos_message_command,new IMqttMessageListener() {
                @Override
                public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                    //----------------------------- FILTER TOPIC NOT CONTAINS -------------------------------//
                            
                                Date HariSekarang_run = new Date();
                                String payload = new String(message.getPayload());

                                String msg_type = "";
                                String message_ADT_Decompress = "";
                                try{
                                    message_ADT_Decompress = gf.ADTDecompress(message.getPayload());
                                    msg_type = "json";
                                }catch(Exception exc){
                                    message_ADT_Decompress = payload;
                                    msg_type = "non json";
                                }
                                
                                String tanggal_jam = gf.get_tanggal_curdate_curtime();
                                gf.WriteFile("timemessage.txt", "", tanggal_jam, false);
                                //System.out.println(message_ADT_Decompress);

                                counter++;
                                UnpackJSON(message_ADT_Decompress);
                                //System.out.println(message_ADT_Decompress);
                                gf.WriteLog("CloseTab : "+message_ADT_Decompress, true);
                                gf.PrintMessage2("RECV > "+rtopic_config+"",counter,msg_type,topic,Parser_TASK,Parser_FROM,Parser_TO,null,HariSekarang_run);
                                if(Parser_SOURCE.equals("IDMCommander")) {
                                	gf.InsTransReport(Parser_TASK,
                        					Parser_ID,
                        					Parser_SOURCE,
                        					Parser_COMMAND,
                        					Parser_OTP,
                        					Parser_TANGGAL_JAM,
                        					Parser_VERSI,
                        					Parser_HASIL,
                        					Parser_FROM,
                        					Parser_TO,
                        					Parser_SN_HDD,
                        					Parser_IP_ADDRESS,
                        					Parser_STATION,
                        					Parser_CABANG,
                        					Parser_NAMA_FILE,
                        					Parser_CHAT_MESSAGE,
                        					Parser_REMOTE_PATH,
                        					Parser_LOCAL_PATH,
                        					Parser_SUB_ID,
                        					Boolean.parseBoolean(gf.en.getTampilkan_query_console()),
                        					"UPDATE",
                        					"transaksi_aktivitas_user");
                                }
                                
                }
            });
    	}catch(Exception exc) {}
    }
    
     
	public void Run() {
		  System.out.println("=================================          START         ==================================");   
	      try {
		  client_transreport =  gf.get_ConnectionMQtt();
	            //---------------------------- COMMAND -----------------------//
	            int qos_message_command = 0;
	            String topic_config[] = gf.en.getTopicSub().split(",");
        		cekAktif(qos_message_command,topic_config[0]);
        		closeTab(qos_message_command,topic_config[1]);
	        }catch(Exception exc){
	            exc.printStackTrace();
	        }  
	    }
}
