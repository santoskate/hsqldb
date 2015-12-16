package org.hsqldb.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Locale;


/**
 * 
 * Internationalization module
 * 
 * @author CatarinaSantos
 * @version 0.1
 *
 */
public class I18nHandler {
	
	private static String msgPropsDir = ".\\";
	private static String localeDefault = Locale.UK.toString();
	
	private static final String MESSAGE_TAG      = "$$";
	private static final String msgPropsName = "custom-messages";
	private static final String msgPropsExt = "txt";
	
//    private static final int bundleHandle =
//        ResourceBundleHandler.getBundleHandle(msgPropsName, null);
    
    public I18nHandler(){}
    
	public static String getMessage(String key, String vars, String locale){
//		String result = ResourceBundleHandler.getString(bundleHandle, key);
		
		String result = "";
		String[] vars_arr = {};
		
		if (locale == null || locale.equals(""))
			locale = Locale.getDefault().toString();
		
		//System.out.println("Get Cache init:" + System.nanoTime());
		result = (String)I18nCache.getInstance().get(locale + key);
		//System.out.println("Get Cache end:" + System.nanoTime());
		if (result == null){
			//System.out.println("Get file init:" + System.nanoTime());
			result = getMessageInFile(key, locale);
			//System.out.println("Get file end:" + System.nanoTime());
			if (result != null && !result.equals("") ){
				I18nCache.getInstance().put(locale+key, result);
				
				if (vars != null)
					vars_arr = vars.split(",");
				
				if (result != null && vars_arr.length > 0)
					result = insertStrings(result, vars_arr);
				
			} else if (result == null){
				if(locale.equals(localeDefault))
//					result = Error.getMessage(7000);
					result = "Default locale file not found";
				else
					return getMessage(key, vars, localeDefault);
			} else {
//				result = Error.getMessage(1100);
				result = "02000 no data";
			}
		} else {
			if (vars != null)
				vars_arr = vars.split(",");
			
			if (result != null && vars_arr.length > 0)
				result = insertStrings(result, vars_arr);
		}
		
		return result;
	}
	
	public static void initCache(){
		String locale = "";
		for (String filename : listFiles()) {
			File bundle = new File(msgPropsDir + filename);
			locale = filename.replaceFirst(msgPropsName, "").replaceFirst("_", "").replaceFirst("." + msgPropsExt, "");
			
			if(locale.equals(""))
				locale = localeDefault;
			
			try{
				BufferedReader br = new BufferedReader(new FileReader(bundle));
				String line = null;
				while ((line = br.readLine()) != null) {
					String[] text = line.split("=", 2);
					if (text.length == 2)
						I18nCache.getInstance().put(locale+text[0], text[1]);
				}
				br.close();
			}catch(Exception e){
				System.out.println(e.getStackTrace().toString());
			}
		}
	}
	
	public static void resetCache(String cacheKey){
    	if(cacheKey == null){
    		I18nCache.getInstance().clear();
    	} else {
    		I18nCache.getInstance().clear(cacheKey);
    	}
    }
	
	private static String getMessageInFile(String key, String locale){
		String retVal = "";
		File bundle = new File(getBundleFilePath(locale));
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(bundle));
		 
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] text = line.split("=");
				if (text[0].equals(key)){
					retVal = "";
					for(int i = 1; i < text.length; i++){
						retVal = retVal + text[i];
					}
					break;
				}
			}
		 
			br.close();
		}catch(FileNotFoundException e){
			retVal = null;
		}catch(Exception e){
			retVal = "";
		}
		return retVal;
	}
	
	private static String getBundleFilePath(String locale){
		String filePath = "";
		
		if (!locale.equals(""))
			filePath = msgPropsDir + msgPropsName + (!locale.equals(localeDefault) ? "_" + locale : "") + "." + msgPropsExt;
		else
			filePath = msgPropsDir + msgPropsName + (!Locale.getDefault().toString().equals(localeDefault) ? "_" + Locale.getDefault() : "") + "." + msgPropsExt;
		
		return filePath;
	}
	
	private static String insertStrings(String message, Object[] add) {

        StringBuffer sb        = new StringBuffer(message.length() + 32);
        int          lastIndex = 0;
        int          escIndex  = message.length();

        for (int i = 0; i < add.length; i++) {
            escIndex = message.indexOf(MESSAGE_TAG, lastIndex);

            if (escIndex == -1) {
                break;
            }

            sb.append(message.substring(lastIndex, escIndex));
            sb.append(add[i] == null ? "null exception message"
                                     : add[i].toString());

            lastIndex = escIndex + MESSAGE_TAG.length();
        }

        escIndex = message.length();

        sb.append(message.substring(lastIndex, escIndex));

        return sb.toString();
    }
	
	private static String[] listFiles() {

        File f = new File(msgPropsDir); // use here your file directory path
        FilenameFilter fileNameFilter = new FilenameFilter() {
            //@Override
            public boolean accept(File dir, String name) {
               return (name.startsWith(msgPropsName) && name.endsWith(msgPropsExt));
            }
         };
        String[] allFiles = f.list(fileNameFilter);
        
        return allFiles;
	}

	public static void setMsgPropsDir(String dir) {
		msgPropsDir = dir;
	}
	
	public static void setLocaleDefault(String locale) {
		localeDefault = locale;
	}
}
