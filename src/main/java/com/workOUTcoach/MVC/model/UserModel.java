package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Authority;
import com.workOUTcoach.entity.ResetToken;
import com.workOUTcoach.entity.User;
import com.workOUTcoach.security.EmailService;
import com.workOUTcoach.utility.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Repository
public class UserModel {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ResetTokenModel resetTokenModel;

    @Autowired
    private EmailService emailService;

    public User getUserByEmail(String email) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            return session.get(User.class, email);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public boolean saveUsersPassword(User user) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            User updateUser = session.get(User.class, user.getEmail());
            updateUser.setPassword(user.getPassword());

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            Logger.logError("Exception during saving user into database");
            return false;
        }

        return true;
    }

    public boolean saveNewUser(User user, Authority authority) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            session.close();

            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authority);
            session.getTransaction().commit();
            session.close();

        } catch (Exception e) {
            Logger.logError("Exception during adding new user into database");
            return false;
        }

        return true;
    }

    public String createUser(String email, String password, String password2, String name, String surname, int hourlyRate, PasswordEncoder passwordEncoder) {
        if (getUserByEmail(email) != null) {
            return "emailError";
        }
        if (validateString(email) && validateString(password) && validateString(name) && validateString(surname)) {
            if (password.equals(password2)) {
                if(hourlyRate>=0) {
                    password = passwordEncoder.encode(password);
                    User user = new User(email, password, name, surname, hourlyRate);
                    Authority authority = new Authority(user);

                    if (saveNewUser(user, authority)) {
                        return "correct";
                    } else {
                        return "databaseError";
                    }
                }
                else {
                    return "hourlyRateError";
                }
            } else {
                return "passwordError";
            }
        } else {
            return "dataError";
        }
    }

    public String resetPasswordFromToken(String token, String password, String confirmPassword, PasswordEncoder passwordEncoder) {
        User user;
        ResetToken resetToken;
        if (validateString(token)) {
            resetToken = resetTokenModel.getByResetToken(token);
            user = getUserByEmail(resetToken.getEmail());

            if (user != null) {
                if (password.equals(confirmPassword)) {
                    user.setPassword(passwordEncoder.encode(password));
                    resetTokenModel.deleteResetToken(user.getEmail());
                    if (saveUsersPassword(user)) {
                        return "correct";
                    } else {
                        return "databaseError";
                    }
                } else {
                    return "differentPasswordsError";
                }
            } else {
                return "userError";
            }
        } else {
            return "tokenLostError";
        }
    }

    public String sendResetToken(String userEmail, HttpServletRequest request) {

        User user = getUserByEmail(userEmail);

        if (user == null) {
            return "userError";
        } else {
            // Generate random 36-character string token for reset password
            ResetToken resetToken = new ResetToken(userEmail, UUID.randomUUID().toString());
            resetTokenModel.addResetToken(resetToken);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("workOUT.coach@wp.pl");
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl + ":8080"
                    + "/reset?token=" + resetToken.getResetToken());

            emailService.sendEmail(passwordResetEmail);
            return "correct";
        }
    }

    private boolean validateString(String text) {
        return text != null && !text.isEmpty();
    }

    public String editUser(String hiddenEmail, String name, String surname, int hourlyRate) {
        User user = new User(hiddenEmail, "", name, surname, hourlyRate);

        if (editUserInDatabase(user)) {
            return "correct";
        } else {
            return "databaseError";
        }
    }

    private boolean editUserInDatabase(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            User updateUser = session.get(User.class, user.getEmail());

            updateUser.setEmail(user.getEmail());
            updateUser.setName(user.getName());
            updateUser.setSurname(user.getSurname());
            updateUser.setHourlyRate(user.getHourlyRate());

            session.getTransaction().commit();

        } catch (Exception e) {
            Logger.logError("Exception during saving user into database");
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public String changePassword(String email, String currentPassword, String newPassword, String confirmNewPassword, PasswordEncoder passwordEncoder) {
        User user;
        if (checkPassword(email, currentPassword, passwordEncoder)) {
            if (newPassword.equals(confirmNewPassword)) {
                newPassword = passwordEncoder.encode(newPassword);
                user = new User(email, newPassword, "", "", 0);
                if (saveUsersPassword(user)) {
                    return "correct";
                } else {
                    return "databaseError";
                }
            } else {
                return "differentPasswordError";
            }
        } else
            return "passwordError";
    }

    private boolean checkPassword(String email, String password, PasswordEncoder passwordEncoder) {
        User user = getUserByEmail(email);
        Logger.log(user.getPassword());
        Logger.log(password);
        if (passwordEncoder.matches(password, user.getPassword()))
            return true;
        else
            return false;
    }
}
