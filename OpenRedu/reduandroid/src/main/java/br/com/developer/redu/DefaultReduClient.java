package br.com.developer.redu;

import java.util.List;

import br.com.developer.redu.models.Chat;
import br.com.developer.redu.models.ChatMessage;
import br.com.developer.redu.models.Course;
import br.com.developer.redu.models.Enrollment;
import br.com.developer.redu.models.Environment;
import br.com.developer.redu.models.File;
import br.com.developer.redu.models.Folder;
import br.com.developer.redu.models.Lecture;
import br.com.developer.redu.models.Progress;
import br.com.developer.redu.models.Space;
import br.com.developer.redu.models.Status;
import br.com.developer.redu.models.Subject;
import br.com.developer.redu.models.User;

import com.google.gson.reflect.TypeToken;


/**
 * @author igor
 * Classe que faz uma implementação concreta do Wrapper. Herda do ReduClient que fez 
 * a implementação das requisições. Objetivo dessa classe é servir como um Factory dos modelos.
 * Aqui são definidos os  parametros do ReduClient  e o seus respectivos tipos no initTypes.
 */
public class DefaultReduClient extends ReduClient<Course, Enrollment, Environment, Space, Subject,
        User, Status, ChatMessage, Chat, Lecture, Folder, File, Progress> {

    public DefaultReduClient(String consumerKey, String consumerSecret) {
        super(consumerKey, consumerSecret);
    }
    public DefaultReduClient(String consumerKey, String consumerSecret, String pin){
        super(consumerKey, consumerSecret, pin);
    }
    
    @Override
    protected void initTypes() {
        super.userClass = User.class;
        super.userList = new TypeToken<List<User>>(){}.getType();
        super.subjectClass = Subject.class;
        super.subjectList = new TypeToken<List<Subject>>(){}.getType();
        super.enrollmentClass = Enrollment.class;
        super.enrollmentList = new TypeToken<List<Enrollment>>(){}.getType();
        super.courseClass = Course.class;
        super.courseList = new TypeToken<List<Course>>(){}.getType();
        super.spaceClass = Space.class;
        super.spaceList = new TypeToken<List<Space>>(){}.getType();
        super.statusClass = Status.class;
        super.statusList = new TypeToken<List<Status>>() {}.getType();
        super.environmentClass = Environment.class;
        super.environmentList = new TypeToken<List<Environment>>(){}.getType();
        super.chatMessageClass = ChatMessage.class;
        super.chatMessageList= new TypeToken<List<ChatMessage>>(){}.getType();
        super.chatClass = Chat.class;
        super.chatList = new TypeToken<List<Chat>>(){}.getType();
        super.lectureClass = Lecture.class;
        super.lectureList = new TypeToken<List<Lecture>>(){}.getType();
        super.folderClass = Folder.class;
        super.folderList = new TypeToken<List<Folder>>(){}.getType();
        super.fileClass = File.class;
        super.fileList = new TypeToken<List<File>>(){}.getType();
        super.progressClass = Progress.class;
        super.progressList = new TypeToken<List<Progress>>(){}.getType();
    }
	
}
