package com.modsen.notificationservice.service;

import com.modsen.notificationservice.dto.message.RideStatusMessage;
import com.modsen.notificationservice.exception.RideStatusTransformException;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static com.modsen.notificationservice.util.MailParams.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class RideStatusMessageToEmailTransformer implements GenericTransformer<RideStatusMessage, MimeMessage> {

    private final JavaMailSender mailSender;
    private final Configuration configuration;

    @Override
    public MimeMessage transform(RideStatusMessage rideStatusMessage) {
        try {
            String subject = String.format(SUBJECT, rideStatusMessage.rideId());
            String to = rideStatusMessage.passengerEmail();
            String emailContent =getEmailContent(rideStatusMessage);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText(emailContent, true);

            log.info("Email is to be sent to passenger '{}' ", to);

            return mimeMessage;
        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Error transforming rideStatusMessage with rideId {}", rideStatusMessage.rideId());
            throw new RideStatusTransformException(rideStatusMessage.rideId(), e.getMessage());
        }
    }

    private String getEmailContent(RideStatusMessage message) throws IOException, TemplateException {
        Map<String, Object> model = Map.of(
                RIDE_ID_PARAM, message.rideId(),
                FIRST_NAME_PARAM, message.passengerFirstName(),
                STATUS_PARAM, message.status().name(),
                START_POINT_PARAM, message.startPoint(),
                DESTINATION_POINT_PARAM, message.destinationPoint(),
                ESTIMATED_COST_PARAM, message.estimatedCost()
        );

        StringWriter writer = new StringWriter();
        configuration.getTemplate(RIDE_STATUS_EMAIL_TEMPLATE)
                .process(model, writer);

        return writer.getBuffer().toString();
    }
}
