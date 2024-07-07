
package jms;

import java.util.function.Consumer;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;


public class EjemploConsumidor implements ExceptionListener {
    
    void processConsumer(){
        //String clientID = "edwin";
        try {
            //Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            
            //Create connection
            Connection connection = connectionFactory.createConnection();
            //connection.start();
            
            connection.setExceptionListener(this);
            
            //Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            //Create the destination(Topic or Queue)
            Destination destination = session.createQueue("MyQUEUE");
            //Destination destination = session.createQueue("MyTopic");
            
            //Create a MessageConsumer from the Session
            MessageConsumer consumer = session.createConsumer(destination);
            
            consumer.setMessageListener(listener);
            connection.start();
                     
        } catch (JMSException e) {
            System.out.println("Caught: "+e);
            e.printStackTrace();
        }
    }
    
    MessageListener listener = new MessageListener(){
        
        public void onMessage(Message msg){
            
            if (msg instanceof TextMessage){
                TextMessage textMessage = (TextMessage)msg;
                String text = null;
                try {
                    text = textMessage.getText();
                } catch (JMSException e) {
                    //Todo Auto-generated catch block
                    e.printStackTrace();
                }   
                System.out.println("Recibido: " + text);
            } else{
                   System.out.println("Recibido: "+ msg); 
             }           
        }
    };
    
     public synchronized void onException(JMSException ex){
        System.out.println("JMS Exception ocurred. Shutting down cliente");
        
    }
    
    public static void main(String[] args) throws Exception{
        EjemploConsumidor c = new EjemploConsumidor();
        System.out.println("Running Consumer...");
        c.processConsumer();
    }
    
}
