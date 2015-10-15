package ru.codeunited.jms.simple;

import com.ibm.mq.jms.MQQueueConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 12.10.15.
 */
public class JmsHelper {

    public static MQQueueConnectionFactory getConnectionFactory() throws JMSException {
        MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
        connectionFactory.setHostName("ubuntu540");
        connectionFactory.setPort(1414);
        connectionFactory.setQueueManager("DEFQM");
        connectionFactory.setTransportType(1); // => MQConstants.TRANSPORT_MQSERIES_CLIENT
        connectionFactory.setCCSID(1208);
        connectionFactory.setChannel("JVM.DEF.SVRCONN");
        return connectionFactory;
    }

    public static Queue resolveQueue(String name, Session session) throws JMSException {
        return session.createQueue(name);
    }

    public static TextMessage copyMessage(Session session, Message message) throws JMSException {
        return copyMessage(session, (TextMessage) message);
    }

    public static TextMessage copyMessage(Session session, TextMessage message) throws JMSException {
        TextMessage copy = session.createTextMessage();

        // copy message body
        copy.setText(message.getText());

        // copy message properties
        Enumeration<String> propertyNames = message.getPropertyNames();
        while (propertyNames.hasMoreElements()) {
            String propName = propertyNames.nextElement();
            Object propValue = message.getObjectProperty(propName);
            copy.setObjectProperty(propName, propValue);
        }

        // copy mqmd
        copy.setJMSCorrelationID(message.getJMSCorrelationID());
        copy.setJMSDeliveryMode(message.getJMSDeliveryMode());
        copy.setJMSExpiration(message.getJMSExpiration());
        copy.setJMSPriority(message.getJMSPriority());
        copy.setJMSType(message.getJMSType());
        copy.setJMSReplyTo(message.getJMSReplyTo());
        // skip other parameters
        return copy;
    }
}
