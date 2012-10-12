package session;

import play.Logger; 
import play.mvc.Http.Request; 
import play.mvc.Http.Session; 
import flexjson.JSONDeserializer; 
import flexjson.JSONSerializer; 
 
public class SessionManager { 
 
	private static JSONSerializer s = new JSONSerializer(); 
 
	public static void init() {
		final Request request = new DummyRequest();
		Context.current.set(new Context(request, new HashMap <String, String>(), new HashMap <String, String>()));

	}
 
    public static void add(String key, Object value) { 
 
        if(value != null) { 
            Session session = Http.Context.current().session(); 
            session.put(key, s.deepSerialize(value)); 
        } else { 
            Logger.info("Value for " + key + " is null"); 
        } 
    } 
 
    public static <T> T get(String key) { 
 
        Session session = Http.Context.current().session(); 
        final String value = session.get(key); 
 
        if (value == null) { 
            return null; 
        } 
 
        return new JSONDeserializer<T>().deserialize(value); 
    } 
 
} 

