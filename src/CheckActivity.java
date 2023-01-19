
public class CheckActivity {
	
	Global_function gf = new Global_function(false);
	int batas_menit = Integer.parseInt(gf.en.getBatasMenit());
	String kode_cabang = gf.en.getCabang();
	
	public CheckActivity() {
			
	}
	
	
	public boolean RestartIDMReporter_by_service(String topic,String command,String nama_service){
        boolean res = false;
        try {
        	
        	String id = gf.get_id(true);
        	String source = "IDMTester";
        	//String command = "systemctl restart LoginService";
        	String otp = "";
        	String tanggal_jam = gf.get_tanggal_curdate_curtime();
        	String versi = "1.0.1";
        	String hasil = "";
        	String from = "IDMTester";
        	String to = "IDMReporter";
        	String sn_hdd = "";
        	String ip_address = "192.168.131.104";
        	String station = "";
        	String cabang = "HO";
        	String file = "";
        	String nama_file = "";
        	String chat_message = "";
        	String remote_path = "";
        	String local_path = "";
        	String sub_id = gf.get_id(false);
        	
			String res_message = gf.CreateMessage("RESTART_SERVICE",id,source,command,otp,tanggal_jam,versi,hasil,from,to,sn_hdd,ip_address,station,cabang,file,nama_file,chat_message,remote_path,local_path,sub_id);
            //System.out.println("MESSAGE NOTIF : "+res_message);
			byte[] convert_message = res_message.getBytes("US-ASCII");
            byte[] bytemessage = gf.compress(convert_message);
            String topic_dest = topic;
        	gf.PublishMessageNotDocumenter(topic_dest,bytemessage,0,res_message,1);
        	gf.WriteLog("Restart "+nama_service+" : SUKSES", true);
        } catch (Exception e) {
            e.printStackTrace();
            res = false;
        }
        
        return res;
    }
	
	public void Restart_Service(String command,String nama_service) {
		try {
			Runtime r = Runtime.getRuntime();
			Process proc = r.exec(command);
			System.out.println("command : "+command);
			gf.WriteLog("Restart "+nama_service+" : SUKSES", true);
		}catch(Exception exc) {
			
		}
	}
	
	
	public void Run() {
		try {
			
			//System.err.println("batas_menit : "+batas_menit);
			String get_last_time_message_incoming = gf.ReadFile("timemessage.txt").split(" ")[1];
			String waktu_kini = gf.get_tanggal_curdate_curtime().split(" ")[1];
			String selisih = gf.get_time_diff(waktu_kini, get_last_time_message_incoming);
			System.err.println("last_message : "+get_last_time_message_incoming+" VS waktu_kini : "+waktu_kini+" Sel. : "+selisih);
			String selisih_menit = selisih.split(":")[1];
			if(Integer.parseInt(selisih_menit) > batas_menit) {
				String command = "systemctl restart "+gf.en.getId_reporter();
				//RestartIDMReporter_by_service("LISTENER_BACKEND_523/",command,"BC_SQL");
				Restart_Service(command, gf.en.getId_reporter());
				Thread.sleep(10000);
				//System.exit(0);
			}
		}catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	
	
		
}
