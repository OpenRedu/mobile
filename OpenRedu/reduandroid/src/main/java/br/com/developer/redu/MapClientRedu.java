package br.com.developer.redu;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.internal.StringMap;
import com.google.gson.reflect.TypeToken;


/**
 * @author igor
 * 
 * Implementação concreta alternativa para o ReduClient, nessa classe todos os recursos são
 * representados pela classe generica StringMap que é basicamente uma accessor para jsons
 */
public class MapClientRedu extends ReduClient<StringMap, StringMap, StringMap, StringMap, StringMap,
        StringMap, StringMap, StringMap, StringMap, StringMap, StringMap, StringMap, StringMap> {
    public MapClientRedu(String consumerKey, String consumerSecret, String pin) {
        super(consumerKey, consumerSecret, pin);
    }

    @Override
    protected void initTypes() {
    	Type stringMapListType = new TypeToken<List<StringMap>>(){}.getType();
    	
        super.userClass = StringMap.class;
        super.userList = stringMapListType;
        super.subjectClass =StringMap.class;
        super.subjectList = stringMapListType;
        super.enrollmentClass = StringMap.class;
        super.enrollmentList = stringMapListType;
        super.courseClass = StringMap.class;
        super.courseList = stringMapListType;
        super.spaceClass = StringMap.class;
        super.spaceList = stringMapListType;
        super.statusClass = StringMap.class;
        super.statusList = stringMapListType;
        super.environmentClass = StringMap.class;
        super.chatMessageClass = StringMap.class;
        super.chatMessageList = stringMapListType;
        super.chatClass = StringMap.class;
        super.chatList = stringMapListType;
        super.lectureClass = StringMap.class;
        super.lectureList = stringMapListType;
        super.folderClass = StringMap.class;
        super.folderList = stringMapListType;
        super.fileClass = StringMap.class;
        super.fileList = stringMapListType;
        super.progressClass = StringMap.class;
        super.progressList = stringMapListType;
        
    }

}
