package jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQConnection;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;


 
public class EjemploProductor implements ExceptionListener {
    void processProducer (){
        
        try {
            //Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            
            //Create connection
            Connection connection = connectionFactory.createConnection();
            connection.start();
            
            //Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            //Create the destination(Topic or Queue)
            Destination destination = session.createQueue("MyQUEUE");
            //Destination destination = session.createQueue("MyTopic");
            
            //Create a MessageProducer from the session to the topic o queue
            MessageProducer producer = session.createProducer(destination);
            
            int i = 0;
            
            while(i < 20){
                //Create Message
                String text="Hola Mundo!" + i;
                TextMessage message = session.createTextMessage(text);
                System.out.println("Mensaje enviado: "+ text);
                producer.send(message);
                i++;
            }
            //Clean up
            session.close();
            connection.close();           
        } catch (Exception e) {
            System.out.println("Caught: "+e);
            e.printStackTrace();
        }
    }
    
    public synchronized void onException(JMSException ex){
        System.out.println("JMS Exception ocurred. Shutting down cliente");
        
    }
    
    public static void main(String[] args) throws Exception{
        EjemploProductor p = new EjemploProductor();
        System.out.println("Running Producer...");
        p.processProducer();
    }
}
