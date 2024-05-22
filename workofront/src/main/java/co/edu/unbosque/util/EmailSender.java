package co.edu.unbosque.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static boolean sendEmail(String email, String username) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.office365.com"); 
        properties.put("mail.smtp.port", "587"); 

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("workoapp@outlook.com", "etqmmbymowxgahqu");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("workoapp@outlook.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Bienvenido a Worko " + username+"!");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("¡Hola!\r\n"
                    + "\r\n"
                    + "Nos alegra darte la bienvenida a Worko, la app que transformará tu forma de entrenar y alcanzar tus metas de fitness. 🎉\r\n"
                    + "\r\n"
                    + "¿Qué puedes esperar de Worko?\r\n"
                    + "\r\n"
                    + "Rutinas Personalizadas: Diseñadas específicamente para ti, ya sea que estés comenzando o seas un atleta avanzado.\r\n"
                    + "Seguimiento Detallado: Registra tus progresos y mantén un control preciso de tus entrenamientos.\r\n"
                    + "Videos Instructivos: Aprende la técnica correcta de cada ejercicio con nuestros videos explicativos.\r\n"
                    + "Comunidad Activa: Conecta con otros usuarios, comparte tus logros y encuentra inspiración para seguir adelante.\r\n"
                    + "Cómo empezar:\r\n"
                    + "\r\n"
                    + "Explora tu Perfil: Completa tu perfil para recibir recomendaciones personalizadas.\r\n"
                    + "Elige tu Rutina: Selecciona entre nuestras variadas rutinas o crea la tuya propia.\r\n"
                    + "Sigue los Instrucciones: Sigue los pasos detallados en cada rutina y aprovecha los consejos de nuestros expertos.\r\n"
                    + "Monitorea tu Progreso: Utiliza nuestras herramientas de seguimiento para ver tu evolución y ajusta tus metas según sea necesario.\r\n"
                    + "Recuerda, tu viaje hacia una vida más saludable y activa comienza ahora. Estamos aquí para apoyarte en cada paso del camino.\r\n"
                    + "\r\n"
                    + "¡Vamos a alcanzar tus objetivos juntos!\r\n"
                    + "\r\n"
                    + "Saludos deportivos,\r\n"
                    + "\r\n"
                    + "El Equipo de Worko\r\n"
                    + "\r\n"
                    + "");

  

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Correo con imagen enviado exitosamente");
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
