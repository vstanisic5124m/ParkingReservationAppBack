package com.parkingshare.auth.service;

import com.parkingshare.auth.entity.ParkingSpace;
import com.parkingshare.auth.entity.ParkingType;
import com.parkingshare.auth.entity.Reservation;
import com.parkingshare.auth.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, yyyy (EEEE)");

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    public void sendReservationConfirmation(User user, Reservation reservation) {
        if (!mailEnabled) {
            logger.info("Email notifications disabled. Skipping reservation confirmation email to {}", user.getEmail());
            return;
        }

        try {
            ParkingSpace space = reservation.getParkingSpace();
            String formattedDate = reservation.getReservationDate().format(DATE_FORMATTER);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Parking Reservation Confirmed - " + formattedDate);

            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(user.getFirstName()).append(" ").append(user.getLastName()).append(",\n\n");
            body.append("Your parking space reservation has been confirmed!\n\n");
            body.append("Reservation Details:\n");
            body.append("- Date: ").append(formattedDate).append("\n");
            body.append("- Parking Type: ").append(space.getParkingType().name()).append("\n");
            body.append("- Spot Number: ").append(space.getSpotNumber()).append("\n");
            body.append("- Reservation ID: ").append(reservation.getId()).append("\n\n");

            if (space.getParkingType() == ParkingType.GARAGE) {
                body.append("IMPORTANT: Please remember to pick up the access card from the counter before you leave today and return it the next day.\n\n");
            } else {
                body.append("IMPORTANT: Please have your ID ready when entering the company yard parking.\n\n");
            }

            body.append("Please note:\n");
            body.append("- If you do not arrive by 9:00 AM on the reservation date, your spot may be released to others.\n");
            body.append("- You can cancel this reservation through the application if needed.\n\n");
            body.append("Thank you for using ParkingShare!\n\n");
            body.append("Best regards,\n");
            body.append("ParkingShare Team");

            message.setText(body.toString());
            mailSender.send(message);

            logger.info("Reservation confirmation email sent to {} for reservation ID {}", user.getEmail(), reservation.getId());
        } catch (Exception e) {
            logger.error("Failed to send reservation confirmation email to {}: {}", user.getEmail(), e.getMessage(), e);
        }
    }

    public void sendReservationCancellation(User user, Reservation reservation) {
        if (!mailEnabled) {
            logger.info("Email notifications disabled. Skipping reservation cancellation email to {}", user.getEmail());
            return;
        }

        try {
            ParkingSpace space = reservation.getParkingSpace();
            String formattedDate = reservation.getReservationDate().format(DATE_FORMATTER);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Parking Reservation Cancelled - " + formattedDate);

            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(user.getFirstName()).append(" ").append(user.getLastName()).append(",\n\n");
            body.append("Your parking space reservation has been cancelled.\n\n");
            body.append("Cancelled Reservation Details:\n");
            body.append("- Date: ").append(formattedDate).append("\n");
            body.append("- Parking Type: ").append(space.getParkingType().name()).append("\n");
            body.append("- Spot Number: ").append(space.getSpotNumber()).append("\n");
            body.append("- Reservation ID: ").append(reservation.getId()).append("\n\n");

            if (space.getParkingType() == ParkingType.GARAGE) {
                body.append("Note: If you already picked up an access card, please return it to the counter.\n\n");
            }

            body.append("You can make a new reservation anytime through the application.\n\n");
            body.append("Thank you for using ParkingShare!\n\n");
            body.append("Best regards,\n");
            body.append("ParkingShare Team");

            message.setText(body.toString());
            mailSender.send(message);

            logger.info("Reservation cancellation email sent to {} for reservation ID {}", user.getEmail(), reservation.getId());
        } catch (Exception e) {
            logger.error("Failed to send reservation cancellation email to {}: {}", user.getEmail(), e.getMessage(), e);
        }
    }

    public void sendOwnerCancellationNotification(User owner, ParkingSpace parkingSpace, LocalDate cancellationDate) {
        if (!mailEnabled) {
            logger.info("Email notifications disabled. Skipping owner cancellation email to {}", owner.getEmail());
            return;
        }

        try {
            String formattedDate = cancellationDate.format(DATE_FORMATTER);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(owner.getEmail());
            message.setSubject("Parking Space Availability Cancelled - " + formattedDate);

            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(",\n\n");
            body.append("You have successfully marked your parking space as unavailable.\n\n");
            body.append("Details:\n");
            body.append("- Date: ").append(formattedDate).append("\n");
            body.append("- Parking Type: ").append(parkingSpace.getParkingType().name()).append("\n");
            body.append("- Spot Number: ").append(parkingSpace.getSpotNumber()).append("\n\n");
            body.append("Your parking space will not be available for reservation by colleagues on this date.\n\n");
            body.append("Thank you for using ParkingShare!\n\n");
            body.append("Best regards,\n");
            body.append("ParkingShare Team");

            message.setText(body.toString());
            mailSender.send(message);

            logger.info("Owner cancellation notification email sent to {} for parking space {} on {}",
                    owner.getEmail(), parkingSpace.getId(), cancellationDate);
        } catch (Exception e) {
            logger.error("Failed to send owner cancellation notification email to {}: {}", owner.getEmail(), e.getMessage(), e);
        }
    }
}
