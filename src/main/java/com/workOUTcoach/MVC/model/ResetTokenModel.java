package com.workOUTcoach.MVC.model;


import com.workOUTcoach.entity.ResetToken;
import com.workOUTcoach.utility.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ResetTokenModel {

    @Autowired
    private SessionFactory sessionFactory;

    public ResetToken getByResetToken(String resetToken) {
        return (ResetToken) sessionFactory.openSession().createQuery("from ResetToken rt where rt.resetToken = '"+resetToken+"'").getResultList().get(0);
    }

    public ResetToken getResetTokenByEmail(String email) {
        //return (ResetToken) sessionFactory.openSession().createQuery("from ResetToken rt where rt.email = '"+email+"'").getResultList().get(0);
        return sessionFactory.openSession().get(ResetToken.class, email);
    }

    public boolean saveResetToken(ResetToken resetToken) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            ResetToken updateResetToken = session.get(ResetToken.class, resetToken.getEmail());
            updateResetToken.setResetToken(resetToken.getResetToken());
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            Logger.logError("Exception during saving ResetToken into database");
            return false;
        }
        return true;
    }

    public boolean addResetToken(ResetToken resetToken) {

        deleteResetToken(resetToken.getEmail());
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(resetToken);
            session.getTransaction().commit();
            session.close();

        } catch (Exception e) {
            Logger.logError("Exception during adding new resetToken into database");
            return false;
        }
        return true;
    }

    public boolean deleteResetToken(String email) {
        ResetToken existingToken = getResetTokenByEmail(email);
        if(existingToken!=null){
            try {
                Session session = sessionFactory.openSession();
                session.beginTransaction();
                session.delete(existingToken);
                session.getTransaction().commit();
                session.close();
            }catch (Exception e){
                Logger.logError("Exception during removing existing resetToken");
                return false;
            }
        }else {
            Logger.log("Reset token doesn't exist");
        }
        return true;
    }
}
